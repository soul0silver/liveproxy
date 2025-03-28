package com.springboot.app.dto.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ProxyType {
    ONE_HOUR(1, 1, 2000),
    ONE_DAY(24,2, 15000),
    ;

    private final int hours;
    private final int key;
    private final int price;

    public static String fromKey(String name) {
        return valueOf(name.toUpperCase()).name();
    }

    public static ProxyType getProxyType(String name) {
        return valueOf(name.toUpperCase());
    }
}
