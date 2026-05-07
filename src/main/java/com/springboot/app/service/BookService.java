package com.springboot.app.service;

import com.springboot.app.dto.BookCriteria;
import com.springboot.app.dto.BookRequest;
import com.springboot.app.entity.Book;
import org.springframework.data.domain.Page;

public interface BookService {
    Page<Book> list(BookCriteria criteria);

    Book getById(Long id);

    Book create(BookRequest request);

    Book update(Long id, BookRequest request);

    void delete(Long id);
}
