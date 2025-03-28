package com.springboot.app.service.impl;

import com.springboot.app.dto.LoginRequest;
import com.springboot.app.dto.RegisterRequest;
import com.springboot.app.entity.Role;
import com.springboot.app.entity.User;
import com.springboot.app.exception.BusinessEx;
import com.springboot.app.repo.OrderRepo;
import com.springboot.app.repo.RoleRepo;
import com.springboot.app.repo.UserRepo;
import com.springboot.app.security.JwtProvider;
import com.springboot.app.security.JwtResponse;
import com.springboot.app.security.user_principle.UserPrincipal;
import com.springboot.app.service.KeyService;
import com.springboot.app.service.UserService;
import com.springboot.app.utils.ContextUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.payos.PayOS;

import java.util.Set;

@Transactional
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final KeyService keyService;
    private final RoleRepo roleRepo;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final PayOS payOS;
    private final OrderRepo orderRepo;

    @Override
    public Long save(RegisterRequest request) {
        if (userRepo.existsByUsername(request.getEmail())) {
            throw new BusinessEx("Email đã tồn tại!", HttpStatus.CONFLICT);
        }

        var user = new User();
        user.setUsername(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role role = roleRepo.findByName("GUEST");
        user.setRoles(Set.of(role));
        user = userRepo.save(user);
        return user.getId();
    }

    @Override
    public User findById(Long id) {
        return null;
    }

    @Override
    public User findByEmail(String email) {
        return userRepo.findByUsername(email)
                .orElseThrow(() -> new BusinessEx("User not found"));
    }

    @Override
    public JwtResponse login(LoginRequest request) {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtProvider.generateToken(authentication);
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            return
                    new JwtResponse(userPrincipal.getUsername(), token, userPrincipal.getWallet());

    }

    @Override
    public UserPrincipal get() {

        return userRepo.findByUsername(ContextUtils.getCurrentUser())
                        .map(UserPrincipal::build)
                .orElseThrow(() -> new BusinessEx("User not found"));
    }

}
