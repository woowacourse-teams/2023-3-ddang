package com.ddang.ddang.chat.application.exception;

public class UserCannotAccessChatRoomException extends IllegalStateException {

    public UserCannotAccessChatRoomException(final String message) {
        super(message);
    }
}
