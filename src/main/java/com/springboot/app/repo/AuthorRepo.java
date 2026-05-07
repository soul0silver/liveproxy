package com.springboot.app.repo;

import com.springboot.app.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepo extends JpaRepository<Author, Long> {
    Optional<Author> findByName(String name);
}
