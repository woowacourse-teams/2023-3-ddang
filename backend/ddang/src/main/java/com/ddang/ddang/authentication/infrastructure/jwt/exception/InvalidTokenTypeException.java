package com.ddang.ddang.authentication.infrastructure.jwt.exception;

public class InvalidTokenTypeException extends IllegalArgumentException {

    public InvalidTokenTypeException(final String message) {
        super(message);
    }

    public InvalidTokenTypeException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
