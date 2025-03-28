package com.springboot.app.controller;

import com.springboot.app.dto.ExtendKeyRequest;
import com.springboot.app.dto.KeyCriteria;
import com.springboot.app.dto.PagingRes;
import com.springboot.app.service.KeyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/keys")
@RequiredArgsConstructor
public class KeyController {
    private final KeyService keyService;

    @PostMapping
    ResponseEntity<PagingRes> list(@RequestBody KeyCriteria pageRequest) {
        return ResponseEntity.ok(PagingRes.of(keyService.list(pageRequest)));
    }

    @DeleteMapping
    ResponseEntity<?> delete(@RequestBody Set<String> keys) {
        keyService.deleteKey(keys);
        return ResponseEntity.ok(null);
    }

    @PutMapping
    ResponseEntity<?> update(@RequestBody @Valid ExtendKeyRequest request) {
        keyService.extendKey(request);
        return ResponseEntity.ok(null);
    }
}
