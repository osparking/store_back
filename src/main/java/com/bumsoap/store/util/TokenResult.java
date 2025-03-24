package com.bumsoap.store.util;

public enum TokenResult {
    INVALID("잘못된 토큰"),
    VERIFIED("이미 검증된 토큰"),
    EXPIRED("만료된 토큰"),
    VALIDATED("계정 활성화");

    public final String label;

    private TokenResult(String label) {
        this.label = label;
    }
}
