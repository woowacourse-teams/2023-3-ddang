package com.ddang.ddang.review.domain.exception;

public class InvalidScoreException extends IllegalArgumentException {

    public InvalidScoreException(final String message) {
        super(message);
    }
}
