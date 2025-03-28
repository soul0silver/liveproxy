package com.springboot.app.controller;

import com.springboot.app.security.user_principle.UserPrincipal;
import com.springboot.app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    ResponseEntity<UserPrincipal> retrieve(){
        return ResponseEntity.ok(userService.get());
    }
}
