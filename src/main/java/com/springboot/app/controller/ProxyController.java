package com.springboot.app.controller;

import com.springboot.app.dto.ProxyRequest;
import com.springboot.app.entity.Proxy;
import com.springboot.app.service.ProxyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/proxies")
@RequiredArgsConstructor
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping
    ResponseEntity<Page<Proxy>> list(ProxyRequest request) {
        return ResponseEntity.ok(proxyService.list(request));
    }

    @PostMapping("/rand")
    ResponseEntity<List<String>> rand(@RequestBody @Valid ProxyRequest request) {
        return ResponseEntity.ok(proxyService.getRandom(request));
    }
 }
