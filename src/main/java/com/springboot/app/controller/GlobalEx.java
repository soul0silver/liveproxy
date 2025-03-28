package com.springboot.app.controller;

import com.springboot.app.exception.BusinessEx;
import com.springboot.app.exception.NotEnoughMoneyEx;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class GlobalEx {
    @ExceptionHandler(BusinessEx.class)
    ResponseEntity<String> handleBusinessEx(BusinessEx ex) {
        ex.printStackTrace();
        if (ex.getStatus() != null) {
            return new ResponseEntity<>(ex.getMessage(),ex.getStatus());
        }
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return new ResponseEntity<>("Username or Password incorrect",HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtException.class)
    ResponseEntity<String> handleExpiredJwtException(JwtException ex) {
        return new ResponseEntity<>("Token is expired",HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(Exception.class)
    ResponseEntity<String> handleException(Exception ex) {
        ex.printStackTrace();
        return new ResponseEntity<>(ex.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(NotEnoughMoneyEx.class)
    ResponseEntity<String> handleNotEnoughMoneyEx(NotEnoughMoneyEx ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
