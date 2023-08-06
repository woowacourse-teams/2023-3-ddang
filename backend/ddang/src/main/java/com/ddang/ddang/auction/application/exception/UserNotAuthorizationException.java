package com.ddang.ddang.auction.application.exception;

public class UserNotAuthorizationException extends IllegalArgumentException {

    public UserNotAuthorizationException(final String message) {
        super(message);
    }
}
