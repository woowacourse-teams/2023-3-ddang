package com.ddang.ddang.qna.application.exception;

public class AnswerNotFoundException extends IllegalArgumentException {

    public AnswerNotFoundException(final String message) {
        super(message);
    }
}
