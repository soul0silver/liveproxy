package com.springboot.app.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Location {
    HANOI("HANOI"), QUANGNINH("QUANGNINH");
    private final String name;

}
