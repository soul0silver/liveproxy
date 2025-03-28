package com.springboot.app.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCriteria {
    private String type;
    private Instant from;
    private Instant to;
    private String content;
    @Min(1)
    private int page;
    @Min(10)
    private int size;

    public int getPage() {
        return page - 1;
    }
}
