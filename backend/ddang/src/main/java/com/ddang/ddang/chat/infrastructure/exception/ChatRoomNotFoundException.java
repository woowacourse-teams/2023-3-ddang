package com.ddang.ddang.chat.infrastructure.exception;

public class ChatRoomNotFoundException extends IllegalArgumentException {

    public ChatRoomNotFoundException(final String message) {
        super(message);
    }
}
