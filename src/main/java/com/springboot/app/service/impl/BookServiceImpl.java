package com.springboot.app.service.impl;

import com.springboot.app.dto.BookCriteria;
import com.springboot.app.dto.BookRequest;
import com.springboot.app.dto.constant.BookStatus;
import com.springboot.app.entity.Author;
import com.springboot.app.entity.Book;
import com.springboot.app.entity.Category;
import com.springboot.app.entity.Publisher;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.AuthorRepo;
import com.springboot.app.repo.BookRepo;
import com.springboot.app.repo.CategoryRepo;
import com.springboot.app.repo.PublisherRepo;
import com.springboot.app.repo.specification.BookSpecification;
import com.springboot.app.service.BookService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class BookServiceImpl implements BookService {
    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final CategoryRepo categoryRepo;
    private final PublisherRepo publisherRepo;

    @Override
    public Page<Book> list(BookCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize());
        return bookRepo.findAll(BookSpecification.getSpecification(criteria), pageable);
    }

    @Override
    public Book getById(Long id) {
        return bookRepo.findById(id)
                .orElseThrow(() -> new BusinessEx("Book not found"));
    }

    @Override
    public Book create(BookRequest request) {
        Book book = mapToBook(new Book(), request);
        return bookRepo.save(book);
    }

    @Override
    public Book update(Long id, BookRequest request) {
        Book book = getById(id);
        book = mapToBook(book, request);
        return bookRepo.save(book);
    }

    @Override
    public void delete(Long id) {
        Book book = getById(id);
        bookRepo.delete(book);
    }

    private Book mapToBook(Book book, BookRequest request) {
        book.setTitle(request.getTitle());
        book.setIsbn(request.getIsbn());
        book.setPrice(request.getPrice());
        book.setDescription(request.getDescription());
        book.setStockQuantity(request.getStockQuantity());
        book.setPublishedDate(request.getPublishedDate());
        book.setLanguage(request.getLanguage());
        book.setPageCount(request.getPageCount());
        book.setCoverImage(request.getCoverImage());

        if (StringUtils.isNotBlank(request.getStatus())) {
            try {
                book.setStatus(BookStatus.valueOf(request.getStatus()));
            } catch (IllegalArgumentException e) {
                throw new BusinessEx("Invalid book status: " + request.getStatus());
            }
        }

        if (request.getPublisherId() != null) {
            Publisher publisher = publisherRepo.findById(request.getPublisherId())
                    .orElseThrow(() -> new BusinessEx("Publisher not found"));
            book.setPublisher(publisher);
        } else {
            book.setPublisher(null);
        }

        if (request.getAuthorIds() != null && !request.getAuthorIds().isEmpty()) {
            Set<Author> authors = new HashSet<>(authorRepo.findAllById(request.getAuthorIds()));
            book.setAuthors(authors);
        } else {
            book.setAuthors(Collections.emptySet());
        }

        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Category> categories = new HashSet<>(categoryRepo.findAllById(request.getCategoryIds()));
            book.setCategories(categories);
        } else {
            book.setCategories(Collections.emptySet());
        }

        return book;
    }
}
