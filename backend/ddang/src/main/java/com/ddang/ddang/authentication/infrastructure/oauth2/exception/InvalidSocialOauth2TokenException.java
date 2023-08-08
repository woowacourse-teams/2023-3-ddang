package com.ddang.ddang.authentication.infrastructure.oauth2.exception;

public class InvalidSocialOauth2TokenException extends IllegalArgumentException {

    public InvalidSocialOauth2TokenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
