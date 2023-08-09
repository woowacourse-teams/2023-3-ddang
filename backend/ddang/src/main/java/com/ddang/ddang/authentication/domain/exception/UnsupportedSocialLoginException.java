package com.ddang.ddang.authentication.domain.exception;

public class UnsupportedSocialLoginException extends IllegalArgumentException {

    public UnsupportedSocialLoginException(final String message) {
        super(message);
    }

    public UnsupportedSocialLoginException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
