package com.springboot.app.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public class BusinessEx extends RuntimeException {
    private String message;
    private HttpStatus status;
    public BusinessEx(String message) {
        this.message = message;
    }
    public BusinessEx() {}
    public BusinessEx(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
