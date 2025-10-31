package com.bumsoap.store.util;

public enum TossPaymentMethod {
    CARD("카드"),
    VIRTUAL_ACCOUNT("가상계좌"),
    EASY_PAYMENT("간편결제"),
    MB_PHONE("휴대폰"),
    ACCOUNT_TRANSFER("계좌이체"),
    CULTURE_GIFT_CERT("문화상품권"),
    BOOK_GIFT_CERT("도서문화상품권"),
    GAME_GIFT_CERT("게임문화상품권");

    private final String label;

    private TossPaymentMethod(String label) {
        this.label = label;
    }

    public static TossPaymentMethod valueOfLabel(String label) {
        for (TossPaymentMethod e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return label;
    }
}