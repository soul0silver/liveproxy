package com.springboot.app.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank
    private String password;
    @NotBlank
    private String email;
    @NotBlank
    private String rePassword;

    @AssertTrue
    public boolean isValid() {
        return password.equals(rePassword);
    }
}
