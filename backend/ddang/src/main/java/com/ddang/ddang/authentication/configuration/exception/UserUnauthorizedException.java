package com.ddang.ddang.authentication.configuration.exception;

public class UserUnauthorizedException extends IllegalArgumentException {

    public UserUnauthorizedException(final String message) {
        super(message);
    }
}
