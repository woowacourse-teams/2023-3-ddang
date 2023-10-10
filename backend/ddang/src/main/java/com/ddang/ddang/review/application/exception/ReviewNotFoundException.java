package com.ddang.ddang.review.application.exception;

public class ReviewNotFoundException extends IllegalArgumentException {

    public ReviewNotFoundException(final String message) {
        super(message);
    }
}
