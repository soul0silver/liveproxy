package com.springboot.app.service.impl;

import com.springboot.app.config.AppProperties;
import com.springboot.app.dto.OrderCriteria;
import com.springboot.app.dto.OrderRequest;
import com.springboot.app.dto.constant.ProxyType;
import com.springboot.app.entity.Order;
import com.springboot.app.entity.User;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.*;
import com.springboot.app.repo.specification.OrderSpecification;
import com.springboot.app.service.KeyService;
import com.springboot.app.service.OrderService;
import com.springboot.app.utils.ContextUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLink;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Transactional
@Service
@AllArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {
    private final UserRepo userRepo;
    private final KeyService keyService;
    private final ProductRepo productRepo;
    private final OrderRepo orderRepo;
    private final PayOS payOS;
    private final RestTemplate restTemplate;
    private final AppProperties appProperties;
    private final PayosRepo payosRepo;
    private final DiscountRepo discountRepo;

    @Override
    public void createOrder(CreatePaymentLinkResponse data, OrderRequest request) {

        Long cid = userRepo.findByUsername(ContextUtils.getCurrentUser())
                .map(User::getId)
                .orElseThrow(BusinessEx::new);

        Optional.ofNullable(request)
                .ifPresentOrElse(
                        req -> {
                            var product = productRepo.findByName(req.getName())
                                    .orElseThrow(BusinessEx::new);
                            var d = discountRepo.findDiscountByType(ProxyType.fromKey(product.getType()));
                            var amount = req.getQuantity() * req.getTimes() * product.getPrice();
                            var order = Order.builder()
                                    .status("PAID")
                                    .amount((req.getQuantity() * req.getTimes() >= d.getQuantity() && amount >= d.getTotal() ?
                                            (long) amount * (100 - d.getPercent()) / 100 : amount))
                                    .cid(cid)
                                    .pid(product.getId())
                                    .keyType(ProxyType.fromKey(product.getType()))
                                    .quantity(req.getQuantity())
                                    .times(req.getTimes())
                                    .content(getContent(ProxyType.fromKey(product.getType()), request))
                                    .build();
                            orderRepo.save(order);
                            userRepo.updateWallet(order.getAmount(), cid);
                            keyService.generateKey(ProxyType.getProxyType(order.getKeyType()), order.getCid(),
                                    order.getQuantity(), order.getTimes());
                        },
                        () -> {
                            if (data != null) {
                                orderRepo.save(Order.builder()
                                        .amount(data.getAmount())
                                        .orderCode(data.getOrderCode())
                                        .status(data.getStatus().getValue())
                                        .cid(cid)
                                        .keyType(null)
                                        .content(getContent(null, null))
                                        .build());
                            }
                        });


    }


    @Override
    public void cancel(Long orderCode) {
        orderRepo.findByOrderCode(orderCode)
                .ifPresentOrElse(
                        orderRepo::delete,
                        () -> {
                            throw new BusinessEx();
                        }
                );
    }

    @Override
    public void recharge(long orderCode) {
        orderRepo.findByOrderCode(orderCode).ifPresentOrElse(
                o -> {
                    try {
                        PaymentLink order = payOS.paymentRequests().get(o.getOrderCode());
                        userRepo.findById(o.getCid())
                                .ifPresentOrElse(
                                        user -> {
                                            user.setWallet(user.getWallet() + order.getAmountPaid());
                                            userRepo.save(user);
                                        },
                                        () -> {
                                            throw new BusinessEx("User not found");
                                        }
                                );
                    } catch (Exception e) {
                        throw new BusinessEx(e.getMessage());
                    }
                },
                () -> {
                    throw new BusinessEx("Order not found");
                }
        );
    }

//    @Scheduled(fixedDelayString = "${app.schedule}", timeUnit = TimeUnit.MINUTES)
//    @Override
//    public void checkRecharge() throws JsonProcessingException {
//        log.info("update order");
//        var orders = orderRepo.findAllByStatus("PENDING", PageRequest.of(0, 50));
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        if (!orders.isEmpty()) {
//            var str = orders.stream()
//                    .map(o -> "orderCode=" + o.getOrderCode())
//                    .collect(Collectors.joining("&"));
//            var url = appProperties.getUrlListOrder() + "?page=0&pageSize=50&" + str;
//            HttpHeaders headers = new HttpHeaders();
//            headers.add("Authorization","Bearer " + payosRepo.findPayosAccByEmail(appProperties.getEmailPayos()).get().getToken());
//            HttpEntity<?> entity = new HttpEntity<>(null, headers);
//            var res = restTemplate.exchange(url, HttpMethod.GET, entity, PayosResponse.class).getBody();
//
//            if (Objects.nonNull(res)) {
//                var data = res.getData().toString();
//                System.out.println("data: " + data);
//                List<PaymentLinkData> list = objectMapper.readValue(data, new TypeReference<List<PaymentLinkData>>() {
//                });
//
//                var updateMap = list.stream().collect(Collectors.toMap(PaymentLinkData::getOrderCode, p -> p));
//                var lastOrders = orders.parallelStream()
//                        .map(o ->
//                                Optional.ofNullable(updateMap.get(o.getOrderCode()))
//                                        .map(value -> {
//                                                    o.setAmount(value.getAmountPaid());
//                                                    o.setStatus(value.getStatus());
//                                                    return o;
//                                                }
//                                        )
//                                        .orElse(o))
//                        .toList();
//                var listUserId = lastOrders.stream()
//                        .filter(o -> o.getStatus().equals("PAID"))
//                        .map(Order::getCid)
//                        .collect(Collectors.toSet());
//                var amountMap = lastOrders.stream()
//                        .filter(o -> o.getStatus().equals("PAID"))
//                        .collect(Collectors
//                        .groupingBy(
//                                Order::getCid,
//                                Collectors.summingInt(Order::getAmount)));
//                orderRepo.saveAll(lastOrders);
//                var users = userRepo.findAllById(listUserId).parallelStream()
//                        .map(u -> Optional.ofNullable(amountMap.get(u.getId()))
//                                .map(a -> {
//                                    u.setWallet(u.getWallet() + a);
//                                    return u;
//                                })
//                                .orElse(u)
//                        )
//                        .toList();
//                userRepo.saveAll(users);
//            }
//        }
//    }

    @Scheduled(fixedDelayString = "${app.schedule}", timeUnit = TimeUnit.MINUTES)
    @Override
    public void checkRecharge() {
        log.info("update order");
        orderRepo.findAllByStatus("PENDING", PageRequest.of(0, 30))
                .parallelStream()
                .forEach(o -> {
                    try {
                        if (Objects.nonNull(o.getOrderCode())) {
                            PaymentLink order = payOS.paymentRequests().get(o.getOrderCode());
                            if (order.getStatus().getValue().equals("PAID")) {
                                o.setStatus("PAID");
                                orderRepo.save(o);
                                userRepo.recharge(order.getAmountPaid(), o.getCid());
                            }

                            if (order.getStatus().getValue().equals("CANCELLED")) {
                                orderRepo.delete(o);
                            }
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    @Override
    public Page<Order> search(OrderCriteria request) {
        Pageable pageable = PageRequest.of(request.getPage(), request.getSize(),
                Sort.by(Sort.Direction.DESC, "createdAt"));
        return orderRepo.findAll(OrderSpecification.search(ContextUtils.getCurrentUserId(), request), pageable);
    }

    private String getContent(String keytype, OrderRequest request) {
        if (keytype == null) {
            return "Nạp tiền";
        }

        switch (ProxyType.getProxyType(keytype)) {
            case ONE_HOUR -> {
                return "Mua " + request.getQuantity() + " key 1 giờ, gia hạn " + request.getTimes() + " giờ";
            }
            case ONE_DAY -> {
                return "Mua " + request.getQuantity() + " key 1 ngày, gia hạn " + request.getTimes() + " ngày";
            }
            default -> {
                return "Nạp tiền";
            }
        }
    }

}
