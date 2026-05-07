package com.springboot.app.entity;

import com.springboot.app.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "authors")
public class Author extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "author_sequence")
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String biography;

    private LocalDate birthDate;
}
