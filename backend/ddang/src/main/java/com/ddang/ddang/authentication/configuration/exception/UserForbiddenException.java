package com.ddang.ddang.authentication.configuration.exception;

public class UserForbiddenException extends IllegalArgumentException {

    public UserForbiddenException(final String message) {
        super(message);
    }
}
