package com.ddang.ddang.chat.application.exception;

public class UserNotAccessibleException extends IllegalStateException {

    public UserNotAccessibleException(final String message) {
        super(message);
    }
}
