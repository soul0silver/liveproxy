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
public class KeyCriteria {
    private String keyProxy;
    private String alias;
    private String type;
    private Boolean active;
    private String sortBy;
    @Min(1)
    private int page = 1;
    @Min(10)
    @Max(50)
    private int size = 10;

    public int getPage() {
        return page - 1;
    }
}
