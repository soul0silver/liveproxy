package com.springboot.app.service;

import com.springboot.app.dto.AuthorRequest;
import com.springboot.app.entity.Author;
import org.springframework.data.domain.Page;

public interface AuthorService {
    Page<Author> list(int page, int size);

    Author getById(Long id);

    Author create(AuthorRequest request);

    Author update(Long id, AuthorRequest request);

    void delete(Long id);
}
