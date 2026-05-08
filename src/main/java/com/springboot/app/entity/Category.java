package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "category_sequence")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private boolean deleted;
}
