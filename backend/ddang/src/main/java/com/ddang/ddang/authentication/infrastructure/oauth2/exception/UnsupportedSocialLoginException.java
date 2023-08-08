package com.ddang.ddang.authentication.infrastructure.oauth2.exception;

public class UnsupportedSocialLoginException extends IllegalArgumentException {

    public UnsupportedSocialLoginException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
