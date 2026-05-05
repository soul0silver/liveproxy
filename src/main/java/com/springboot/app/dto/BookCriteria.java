package com.springboot.app.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookCriteria {
    private String title;
    private Long authorId;
    private Long categoryId;
    private Long publisherId;
    private Integer minPrice;
    private Integer maxPrice;
    private String status;
    @Min(1)
    private int page = 1;
    @Min(10)
    @Max(50)
    private int size = 10;

    public int getPage() {
        return page - 1;
    }
}
