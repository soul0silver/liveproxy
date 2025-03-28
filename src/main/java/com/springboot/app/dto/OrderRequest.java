package com.springboot.app.dto;

import com.springboot.app.dto.constant.ProxyType;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderRequest {
    @NotBlank
    private String type;
    @Min(1)
    private int quantity;
    @Min(1)
    private int times;
    @NotBlank
    private String name;

    @AssertTrue
    public boolean isValid() {
        return ProxyType.getProxyType(type) != null;
    }
}
