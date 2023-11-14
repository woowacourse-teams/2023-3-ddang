package com.ddang.ddang.auction.infrastructure.persistence.exception;

public class AuctionNotFoundException extends IllegalArgumentException {

    public AuctionNotFoundException(final String message) {
        super(message);
    }
}
