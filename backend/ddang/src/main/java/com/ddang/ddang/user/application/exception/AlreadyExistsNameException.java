package com.ddang.ddang.user.application.exception;

public class AlreadyExistsNameException extends IllegalArgumentException {

    public AlreadyExistsNameException(final String message) {
        super(message);
    }
}
