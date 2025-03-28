package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@Entity
@Table(name = "product")
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name="product_sequence")
    private Long id;
    private String name;
    private String type;
    private int price;
    private String description;
}
