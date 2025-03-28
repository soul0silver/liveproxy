package com.springboot.app.dto;

import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingRes {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private List<?> pageData;

    public static PagingRes of(Page<?> page) {
        return PagingRes.builder()
                .pageNumber(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pageData(page.getContent())
                .build();
    }
}
