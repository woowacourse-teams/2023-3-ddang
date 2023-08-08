package com.ddang.ddang.authentication.application.exception;

public class InvalidTokenException extends IllegalArgumentException {

    public InvalidTokenException(final String message) {
        super(message);
    }
}
