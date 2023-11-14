package com.ddang.ddang.review.infrastructure.exception;

public class ReviewNotFoundException extends IllegalArgumentException {

    public ReviewNotFoundException(final String message) {
        super(message);
    }
}
