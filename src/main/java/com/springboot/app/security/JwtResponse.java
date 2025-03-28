package com.springboot.app.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Setter
@Getter

@NoArgsConstructor
public class JwtResponse {
    private String email;
    private String type="Bearer";
    private long wallet;
    private String token;

    public JwtResponse( String email, String token, long wallet) {
        this.email = email;
        this.token = token;
        this.wallet=wallet;
    }
}
