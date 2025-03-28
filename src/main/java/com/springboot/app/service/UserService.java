package com.springboot.app.service;

import com.springboot.app.dto.LoginRequest;
import com.springboot.app.dto.RegisterRequest;
import com.springboot.app.entity.User;
import com.springboot.app.security.JwtResponse;
import com.springboot.app.security.user_principle.UserPrincipal;

public interface UserService {
    Long save(RegisterRequest request);
    User findById(Long id);
    User findByEmail(String email);
    JwtResponse login(LoginRequest request);
    UserPrincipal get();
}
