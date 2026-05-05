package com.springboot.app.repo;

import com.springboot.app.entity.Publisher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PublisherRepo extends JpaRepository<Publisher, Long> {
    Optional<Publisher> findByName(String name);
}
