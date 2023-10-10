package com.ddang.ddang.qna.application.exception;

public class QuestionNotFoundException extends IllegalArgumentException {

    public QuestionNotFoundException(final String message) {
        super(message);
    }
}
