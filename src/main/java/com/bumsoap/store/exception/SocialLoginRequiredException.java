package com.bumsoap.store.exception;

public class SocialLoginRequiredException extends RuntimeException {
    public SocialLoginRequiredException(String message) {
        super(message);
    }
}
