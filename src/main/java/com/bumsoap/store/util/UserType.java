package com.bumsoap.store.util;

public enum UserType {
    CUSTOMER("고객"),
    WORKER("노동자"),
    ADMIN("관리자");

    public final String label;

    private UserType(String label) {
        this.label = label;
    }
}
