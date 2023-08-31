package com.ddang.ddang.user.application.exception;

public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(final String message) {
        super(message);
    }
}
