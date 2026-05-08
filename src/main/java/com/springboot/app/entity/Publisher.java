package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "publishers")
public class Publisher extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "publisher_sequence")
    private Long id;

    private String name;
    private String address;
    private String phone;
    private String email;
    private boolean deleted;
}
