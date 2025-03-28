package com.springboot.app.controller;

import com.springboot.app.entity.Discount;
import com.springboot.app.repo.DiscountRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/discount")
@RequiredArgsConstructor
public class DiscountController {
    private final DiscountRepo discountRepo;
    
    @GetMapping
    ResponseEntity<List<Discount>> getDiscounts() {
        return ResponseEntity.ok(discountRepo.findAll());
    }
}
