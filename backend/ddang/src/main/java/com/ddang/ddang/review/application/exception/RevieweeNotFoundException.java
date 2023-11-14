package com.ddang.ddang.review.application.exception;

public class RevieweeNotFoundException extends IllegalArgumentException {

    public RevieweeNotFoundException(final String message) {
        super(message);
    }
}
