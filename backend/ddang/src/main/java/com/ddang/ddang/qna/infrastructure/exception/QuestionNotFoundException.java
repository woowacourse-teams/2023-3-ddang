package com.ddang.ddang.qna.infrastructure.exception;

public class QuestionNotFoundException extends IllegalArgumentException {

    public QuestionNotFoundException(final String message) {
        super(message);
    }
}
