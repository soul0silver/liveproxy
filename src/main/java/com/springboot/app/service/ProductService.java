package com.springboot.app.service;

import com.springboot.app.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<Product> list();
}
