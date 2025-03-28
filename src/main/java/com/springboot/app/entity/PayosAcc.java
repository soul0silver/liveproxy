package com.springboot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "payos_acc")
@Entity
public class PayosAcc {
    @Id
    private int code;
    private String token;
    @Column(name = "refresh_token")
    private String refreshToken;
    private String id;
    private String first_name;
    private String last_name;
    private String email;
    private String avatar_url;
    @Column(name = "expires_at")
    private long expiresAt;
}
