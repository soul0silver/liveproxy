package com.springboot.app.controller;

import com.springboot.app.dto.LoginRequest;
import com.springboot.app.dto.RegisterRequest;
import com.springboot.app.entity.User;
import com.springboot.app.security.JwtResponse;
import com.springboot.app.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;

    @PostMapping("/register")
    Long register(@RequestBody @Valid RegisterRequest user) {
        return userService.save(user);
    }

    @PostMapping("/login")
    ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return ResponseEntity.ok(userService.login(loginRequest));
    }
}
