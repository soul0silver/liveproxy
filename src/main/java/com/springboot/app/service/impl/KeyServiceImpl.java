package com.springboot.app.service.impl;

import com.springboot.app.dto.ExtendKeyRequest;
import com.springboot.app.dto.KeyCriteria;
import com.springboot.app.dto.constant.Location;
import com.springboot.app.dto.constant.ProxyType;
import com.springboot.app.entity.CustomerKey;
import com.springboot.app.entity.Product;
import com.springboot.app.entity.User;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.exception.NotEnoughMoneyEx;
import com.springboot.app.repo.DiscountRepo;
import com.springboot.app.repo.KeyRepo;
import com.springboot.app.repo.ProductRepo;
import com.springboot.app.repo.UserRepo;
import com.springboot.app.repo.specification.KeySpecification;
import com.springboot.app.service.KeyService;
import com.springboot.app.utils.ContextUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Transactional
@Service
@RequiredArgsConstructor
public class KeyServiceImpl implements KeyService {
    private final KeyRepo keyRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserRepo userRepo;
    private final ProductRepo productRepo;
    private final DiscountRepo discountRepo;

    @Override
    public void generateKey(ProxyType type, Long cid, int quantity, int times) {
        List<CustomerKey> list = new ArrayList<>();
        var products = productRepo.findAll().stream()
                .collect(Collectors.toMap(Product::getType, Product::getPrice));
        IntStream.range(0, quantity).forEach(i -> {
            CustomerKey customerKey = CustomerKey.builder()
                    .customerId(cid)
                    .issued(Instant.now())
                    .outdated(Instant.now().plusSeconds(type.getHours() * 3600L * times))
                    .keyProxy(passwordEncoder.encode(type.name() + cid))
                    .type(type.name())
                    .price(products.get(type.name()))
                    .build();
            list.add(customerKey);
        });

        keyRepo.saveAll(list);
    }

    @Override
    public Page<CustomerKey> list(KeyCriteria criteria) {
        var cid = userRepo.findByUsername(ContextUtils.getCurrentUser())
                .map(User::getId)
                .orElseThrow(BusinessEx::new);

        Sort sort = StringUtils.isNotBlank(criteria.getSortBy()) ?
                Sort.by(Sort.Direction.DESC, criteria.getSortBy()) :
                Sort.by(Sort.Direction.DESC, "outdated");

        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), sort);
        return keyRepo.findAll(KeySpecification.keyEquals(cid, criteria), pageable);
    }

    @Override
    public void updateKey(Set<CustomerKey> customerKeys) {
        var clone = new ArrayList<>(customerKeys).get(0);
        Set<Long> ids = customerKeys.stream().map(CustomerKey::getId).collect(Collectors.toSet());
        if (keyRepo.findCustomerKeyByIdInAndCustomerId(ids, ContextUtils.getCurrentUserId()).size() != ids.size()) {
            throw new BusinessEx("Invalid keys");
        }

        keyRepo.updateCustomerKey(ids, clone.getAlias(), clone.getOutdated());

    }

    @Override
    public void deleteKey(Set<String> keys) {
        List<CustomerKey> keyList = keyRepo.findAllByCustomerIdAndKeyProxyIn(ContextUtils.getCurrentUserId(), keys);

        if (keyList.size() != keys.size()) {
            throw new BusinessEx("Invalid keys");
        }

        keyRepo.deleteAll(keyList);
    }

    @Override
    public void extendKey(ExtendKeyRequest request) {
        List<CustomerKey> keyList = keyRepo.findAllByCustomerIdAndKeyProxyIn(ContextUtils.getCurrentUserId(), request.getKeys());
        if (keyList.size() != request.getKeys().size()) {
            throw new BusinessEx("Invalid keys");
        }

        keyList.forEach(key -> {
            key.setOutdated(getAddTime(key.getOutdated(), request.getTimes(), key.getType()));
        });

        var discounts = discountRepo.findAll();

        keyRepo.saveAll(keyList);
        userRepo.findByUsername(ContextUtils.getCurrentUser())
                .ifPresentOrElse(
                        user -> {
                            var count = keyList.stream()
                                    .map(k -> {
                                        discounts.stream()
                                                .filter(d -> d.getType().equals(k.getType()))
                                                .findFirst()
                                                .ifPresent(discount -> {
                                                    if (request.getTimes() >= discount.getQuantity() && k.getPrice() * request.getTimes() >= discount.getTotal()) {
                                                        k.setPrice(k.getPrice() * (100 - discount.getPercent()) / 100);
                                                    }
                                                });
                                        return k;
                                    })
                                    .mapToInt(CustomerKey::getPrice).sum() * request.getTimes();


                            if (count < 0) {
                                throw new NotEnoughMoneyEx("Not enough money");
                            }

                            user.setWallet(user.getWallet() - count);
                            userRepo.save(user);
                        },
                        () -> {throw new BusinessEx("Invalid user");}
                );
    }

    private Instant getAddTime(Instant current, int times, String type) {
        if (current.isBefore(Instant.now())) {
            current = Instant.now();
        }
        switch (type) {
            case "ONE_DAY" -> {
                return current.plusSeconds( times * 24L * 3600L);
            }
            case "ONE_HOUR" -> {
                return current.plusSeconds(times * 3600L);
            }
            default -> {
                return current;
            }
        }
    }
}
