package com.ddang.ddang.chat.application.exception;

public class InvalidUserToChat extends IllegalStateException {

    public InvalidUserToChat(final String message) {
        super(message);
    }
}
