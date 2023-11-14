package com.ddang.ddang.qna.infrastructure.exception;

public class AnswerNotFoundException extends IllegalArgumentException {

    public AnswerNotFoundException(final String message) {
        super(message);
    }
}
