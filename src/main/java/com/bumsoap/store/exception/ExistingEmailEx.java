package com.bumsoap.store.exception;

public class ExistingEmailEx extends RuntimeException {
    public ExistingEmailEx(String message) {
        super(message);
    }
}
