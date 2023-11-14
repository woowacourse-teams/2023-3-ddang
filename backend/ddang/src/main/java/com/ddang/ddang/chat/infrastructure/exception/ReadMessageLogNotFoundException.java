package com.ddang.ddang.chat.infrastructure.exception;

public class ReadMessageLogNotFoundException extends IllegalArgumentException {

    public ReadMessageLogNotFoundException(final String message) {
        super(message);
    }
}
