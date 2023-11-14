package com.ddang.ddang.chat.application.exception;

public class SenderNotFoundException extends IllegalArgumentException {

    public SenderNotFoundException(final String message) {
        super(message);
    }
}
