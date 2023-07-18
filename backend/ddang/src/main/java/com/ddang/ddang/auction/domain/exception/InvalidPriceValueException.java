package com.ddang.ddang.auction.domain.exception;

public class InvalidPriceValueException extends IllegalArgumentException {

    public InvalidPriceValueException(final String message) {
        super(message);
    }
}
