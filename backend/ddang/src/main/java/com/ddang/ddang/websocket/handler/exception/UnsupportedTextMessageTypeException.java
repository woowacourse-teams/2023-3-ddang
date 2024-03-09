package com.ddang.ddang.websocket.handler.exception;

public class UnsupportedTextMessageTypeException extends IllegalStateException {

    public UnsupportedTextMessageTypeException(final String message) {
        super(message);
    }
}
