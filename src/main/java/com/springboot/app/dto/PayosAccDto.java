package com.springboot.app.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PayosAccDto {
    private String token;
    private String refreshToken;
    private String id;
    private long expiresAt;
    private String first_name;
    private String last_name;
    private String email;
    private String avatar_url;
}
