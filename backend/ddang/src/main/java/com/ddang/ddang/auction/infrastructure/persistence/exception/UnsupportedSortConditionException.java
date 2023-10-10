package com.ddang.ddang.auction.infrastructure.persistence.exception;

public class UnsupportedSortConditionException extends IllegalArgumentException {

    public UnsupportedSortConditionException(final String message) {
        super(message);
    }
}
