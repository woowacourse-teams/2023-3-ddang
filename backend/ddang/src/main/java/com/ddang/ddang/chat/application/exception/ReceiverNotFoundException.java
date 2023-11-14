package com.ddang.ddang.chat.application.exception;

public class ReceiverNotFoundException extends IllegalArgumentException {

    public ReceiverNotFoundException(final String message) {
        super(message);
    }
}
