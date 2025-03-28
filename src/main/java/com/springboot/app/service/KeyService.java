package com.springboot.app.service;

import com.springboot.app.dto.ExtendKeyRequest;
import com.springboot.app.dto.KeyCriteria;
import com.springboot.app.dto.constant.ProxyType;
import com.springboot.app.entity.CustomerKey;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

public interface KeyService {
    void generateKey(ProxyType type, Long cid, int quantity, int times);
    Page<CustomerKey> list(KeyCriteria criteria);
    void updateKey(Set<CustomerKey> customerKey);
    void deleteKey(Set<String> keys);
    void extendKey(ExtendKeyRequest request);
}
