package com.ddang.ddang.auction.domain.exception;

public class WinnerNotFoundException extends IllegalArgumentException {

    public WinnerNotFoundException(final String message) {
        super(message);
    }
}
