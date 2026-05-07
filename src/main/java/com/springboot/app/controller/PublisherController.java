package com.springboot.app.controller;

import com.springboot.app.dto.PagingRes;
import com.springboot.app.dto.PublisherRequest;
import com.springboot.app.service.PublisherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {
    private final PublisherService publisherService;

    @GetMapping
    ResponseEntity<PagingRes> list(@RequestParam(defaultValue = "0") int page,
                                   @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(PagingRes.of(publisherService.list(page, size)));
    }

    @GetMapping("/{id}")
    ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(publisherService.getById(id));
    }

    @PostMapping
    ResponseEntity<?> create(@RequestBody @Valid PublisherRequest request) {
        return ResponseEntity.ok(publisherService.create(request));
    }

    @PutMapping("/{id}")
    ResponseEntity<?> update(@PathVariable Long id, @RequestBody @Valid PublisherRequest request) {
        return ResponseEntity.ok(publisherService.update(id, request));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> delete(@PathVariable Long id) {
        publisherService.delete(id);
        return ResponseEntity.ok(null);
    }
}
