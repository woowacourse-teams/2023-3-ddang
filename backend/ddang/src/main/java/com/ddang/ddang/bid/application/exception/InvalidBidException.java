package com.ddang.ddang.bid.application.exception;

public class InvalidBidException extends IllegalArgumentException {

    public InvalidBidException(final String message) {
        super(message);
    }
}
