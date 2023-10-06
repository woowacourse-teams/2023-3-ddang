package com.ddang.ddang.review.application.exception;

public class InvalidUserToReview extends IllegalArgumentException {

    public InvalidUserToReview(final String message) {
        super(message);
    }
}
