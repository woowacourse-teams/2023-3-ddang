package com.ddang.ddang.image.infrastructure.persistence.exception;

public class AuctionImageNotFoundException extends IllegalArgumentException {

    public AuctionImageNotFoundException(final String message) {
        super(message);
    }
}
