package com.ddang.ddang.chat.application.exception;

public class ForbiddenChattingUserException extends IllegalStateException {

    public ForbiddenChattingUserException(final String message) {
        super(message);
    }
}
