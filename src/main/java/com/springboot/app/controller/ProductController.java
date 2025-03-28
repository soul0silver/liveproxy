package com.springboot.app.controller;

import com.springboot.app.dto.PagingRes;
import com.springboot.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @PostMapping
    ResponseEntity<PagingRes> list() {
        return ResponseEntity.ok(PagingRes.of(productService.list()));
    }
}
