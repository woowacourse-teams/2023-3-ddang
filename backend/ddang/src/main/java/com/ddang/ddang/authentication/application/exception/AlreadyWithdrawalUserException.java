package com.ddang.ddang.authentication.application.exception;

public class AlreadyWithdrawalUserException extends IllegalArgumentException {

    public AlreadyWithdrawalUserException(final String message) {
        super(message);
    }
}
