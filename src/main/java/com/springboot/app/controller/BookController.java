package com.springboot.app.controller;

import com.springboot.app.dto.BookCriteria;
import com.springboot.app.dto.BookRequest;
import com.springboot.app.dto.PagingRes;
import com.springboot.app.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @PostMapping("/_search")
    ResponseEntity<PagingRes> list(@RequestBody BookCriteria criteria) {
        return ResponseEntity.ok(PagingRes.of(bookService.list(criteria)));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getById(id));
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody @Valid BookRequest request) {
        return ResponseEntity.ok(bookService.create(request));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid BookRequest request) {
        return ResponseEntity.ok(bookService.update(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        bookService.delete(id);
        return ResponseEntity.ok(null);
    }
}
