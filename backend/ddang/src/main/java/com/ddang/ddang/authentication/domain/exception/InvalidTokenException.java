package com.ddang.ddang.authentication.domain.exception;

public class InvalidTokenException extends IllegalArgumentException {

    public InvalidTokenException(final String message) {
        super(message);
    }

    public InvalidTokenException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
