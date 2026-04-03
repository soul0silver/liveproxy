package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_code")
    private Long orderCode;
    private String status;
    private Long amount;
    private Long pid;
    @Column(name = "key_type")
    private String keyType;
    private Long cid;
    private String content;
    private int quantity;
    private int times;
}
