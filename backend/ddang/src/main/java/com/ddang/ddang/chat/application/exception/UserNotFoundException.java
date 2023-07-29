package com.ddang.ddang.chat.application.exception;

public class UserNotFoundException extends IllegalArgumentException {

    public UserNotFoundException(final String message) {
        super(message);
    }
}
