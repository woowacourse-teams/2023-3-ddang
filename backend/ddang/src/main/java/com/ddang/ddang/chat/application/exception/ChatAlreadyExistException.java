package com.ddang.ddang.chat.application.exception;

public class ChatAlreadyExistException extends IllegalArgumentException {

    public ChatAlreadyExistException(final String message) {
        super(message);
    }
}
