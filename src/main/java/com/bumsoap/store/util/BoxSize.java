package com.bumsoap.store.util;

public enum BoxSize {
    BOX_03("03개상자"),
    BOX_12("12개상자");

    public final String label;

    public static BoxSize valueOfLabel(String label) {
        for (BoxSize e : values()) {
            if (e.label.equals(label)) {
                return e;
            }
        }
        return null;
    }

    private BoxSize(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
