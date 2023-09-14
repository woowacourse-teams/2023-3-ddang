package com.ddang.ddang.authentication.application.exception;

public class InaccessibleWithdrawalException extends IllegalArgumentException {

    public InaccessibleWithdrawalException(final String message) {
        super(message);
    }
}
