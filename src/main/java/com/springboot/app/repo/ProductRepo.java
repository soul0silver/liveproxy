package com.springboot.app.repo;

import com.springboot.app.entity.Product;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepo extends JpaRepository<Product, Long> {
    Optional<Product> findByName(String name);

    Optional<Product> findByType(@NotBlank String type);
}
