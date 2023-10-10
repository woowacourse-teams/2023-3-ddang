package com.ddang.ddang.authentication.application.exception;

public class InvalidWithdrawalException extends IllegalArgumentException {

    public InvalidWithdrawalException(final String message) {
        super(message);
    }
}
