package com.springboot.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookRequest {
    @NotBlank
    private String title;
    private String isbn;
    @NotNull
    private int price;
    private String description;
    private int stockQuantity;
    private LocalDate publishedDate;
    private String language;
    private int pageCount;
    private String coverImage;
    private String status;
    private Long publisherId;
    private Set<Long> authorIds;
    private Set<Long> categoryIds;
}
