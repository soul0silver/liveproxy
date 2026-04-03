package com.springboot.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.springboot.app.dto.OrderCriteria;
import com.springboot.app.dto.OrderRequest;
import com.springboot.app.dto.PagingRes;
import com.springboot.app.dto.RechargeRequest;
import com.springboot.app.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.v2.paymentRequests.PaymentLinkItem;


import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final PayOS payOS;
    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @PostMapping("/recharge")
    public ObjectNode createPaymentLink(@RequestBody RechargeRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            String productName = "Recharge";
            String description = "Recharge";

            // Gen order code
            String currentTimeString = String.valueOf(new Date().getTime());
            long orderCode = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
            String returnUrl = "http://localhost:8002/api/v1/pay/success/" + orderCode;
            String cancelUrl = "http://localhost:8002/api/v1/pay/cancel/" + orderCode;
            CreatePaymentLinkRequest paymentData =
                    CreatePaymentLinkRequest.builder()
                            .orderCode(orderCode)
                            .amount(request.getAmount())
                            .description(description)
                            .returnUrl(returnUrl)
                            .cancelUrl(cancelUrl)
                            .item(PaymentLinkItem.builder().name(productName).price(request.getAmount()).quantity(1).build())
                            .build();
            CreatePaymentLinkResponse data = payOS.paymentRequests().create(paymentData);
            orderService.createOrder(data, null);
            response.put("error", 0);
            response.put("message", "success");
            response.set("data", objectMapper.valueToTree(data));
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", "fail");
            response.set("data", null);
            return response;

        }
    }

    @PostMapping
    ResponseEntity<?> buyKey(@RequestBody OrderRequest request) {
        orderService.createOrder(null, request);
        return ResponseEntity.ok(null);
    }

    @GetMapping(path = "/{orderId}")
    public ObjectNode getOrderById(@PathVariable("orderId") long orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();

        try {
            var order = payOS.paymentRequests().get(orderId);

            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }

    }

    @PutMapping(path = "/{orderId}")
    public ObjectNode cancelOrder(@PathVariable("orderId") int orderId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var order = payOS.paymentRequests().cancel(String.valueOf(orderId));
            response.set("data", objectMapper.valueToTree(order));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping(path = "/confirm-webhook")
    public ObjectNode confirmWebhook(@RequestBody Map<String, String> requestBody) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode response = objectMapper.createObjectNode();
        try {
            var str = payOS.webhooks().confirm(requestBody.get("webhookUrl"));
            response.set("data", objectMapper.valueToTree(str));
            response.put("error", 0);
            response.put("message", "ok");
            return response;
        } catch (Exception e) {
            e.printStackTrace();
            response.put("error", -1);
            response.put("message", e.getMessage());
            response.set("data", null);
            return response;
        }
    }

    @PostMapping("/_search")
    ResponseEntity<?> search(@RequestBody @Valid OrderCriteria request) {
        return ResponseEntity.ok(PagingRes.of(orderService.search(request)));
    }

}
