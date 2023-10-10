package com.ddang.ddang.authentication.domain.exception;

public class EmptyTokenException extends IllegalArgumentException {

    public EmptyTokenException(final String message) {
        super(message);
    }
}
