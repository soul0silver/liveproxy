package com.springboot.app.service.impl;

import com.springboot.app.entity.Product;
import com.springboot.app.repo.ProductRepo;
import com.springboot.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    @Override
    public Page<Product> list() {
        return productRepo.findAll(PageRequest.of(0, 10));
    }
}
