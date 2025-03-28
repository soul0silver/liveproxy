package com.springboot.app.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProxyStatus {
    USED(2), FREE(1), NOTWORK(3);
    private final int value;
}
