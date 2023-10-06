package com.ddang.ddang.review.application.exception;

public class AlreadyReviewException extends IllegalArgumentException {

    public AlreadyReviewException(final String message) {
        super(message);
    }
}
