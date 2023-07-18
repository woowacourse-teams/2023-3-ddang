package com.ddang.ddang.actuion.domain.exception;

public class InvalidPriceValueException extends IllegalArgumentException {

    public InvalidPriceValueException(final String message) {
        super(message);
    }
}
