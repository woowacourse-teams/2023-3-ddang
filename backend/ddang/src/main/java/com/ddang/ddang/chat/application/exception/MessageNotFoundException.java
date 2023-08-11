package com.ddang.ddang.chat.application.exception;

public class MessageNotFoundException extends IllegalArgumentException {

    public MessageNotFoundException(final String message) {
        super(message);
    }
}
