package com.springboot.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "in_use_proxy")
public class InUsePort {
    @Id
    @Column(name = "key_proxy")
    private String key;
    private String port;
    @Column(name = "out_date")
    private Instant outDate;
    @Column(name = "device_id")
    private String deviceId;
}
