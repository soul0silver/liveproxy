package com.springboot.app.repo;

import com.springboot.app.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, Integer> {
    Discount findDiscountByType(String type);
}
