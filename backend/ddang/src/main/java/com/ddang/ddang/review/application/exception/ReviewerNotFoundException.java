package com.ddang.ddang.review.application.exception;

public class ReviewerNotFoundException extends IllegalArgumentException {

    public ReviewerNotFoundException(final String message) {
        super(message);
    }
}
