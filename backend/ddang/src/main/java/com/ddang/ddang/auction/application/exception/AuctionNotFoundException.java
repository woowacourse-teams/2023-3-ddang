package com.ddang.ddang.auction.application.exception;

public class AuctionNotFoundException extends IllegalArgumentException {

    public AuctionNotFoundException(final String message) {
        super(message);
    }
}
