package com.ddang.ddang.user.infrastructure.exception;

public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(final String message) {
        super(message);
    }
}
