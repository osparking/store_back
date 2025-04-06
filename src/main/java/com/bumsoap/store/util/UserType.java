package com.bumsoap.store.util;

public enum UserType {
    CUSTOMER("고객"),
    WORKER("노동자"),
    ADMIN("관리자");

    public final String label;

    public static UserType valueOfLabel(String label) {
        for (UserType e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }

    private UserType(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
