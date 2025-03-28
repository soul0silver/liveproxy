package com.springboot.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.springboot.app.dto.OrderCriteria;
import com.springboot.app.dto.OrderRequest;
import com.springboot.app.entity.Order;
import org.springframework.data.domain.Page;
import vn.payos.type.CheckoutResponseData;

public interface OrderService {
    void createOrder(CheckoutResponseData data, OrderRequest request);

    void cancel(Long orderCode);

    void recharge(long orderCode) throws Exception;

    void checkRecharge() throws JsonProcessingException;

    Page<Order> search(OrderCriteria request);
}