package com.springboot.app.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProxyRequest {
    @Size(min = 1)
    private Set<String> keys;
    private String location;

    public String getLocation() {
        return location == null ? "" : location.trim();
    }
}
