package com.ddang.ddang.auction.application.exception;

public class UserForbiddenException extends IllegalArgumentException {

    public UserForbiddenException(final String message) {
        super(message);
    }
}
