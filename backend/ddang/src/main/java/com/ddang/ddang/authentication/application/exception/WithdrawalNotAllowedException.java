package com.ddang.ddang.authentication.application.exception;

public class WithdrawalNotAllowedException extends IllegalArgumentException {

    public WithdrawalNotAllowedException(final String message) {
        super(message);
    }
}
