package com.ddang.ddang.qna.application.exception;

public class AlreadyAnsweredException extends IllegalArgumentException {

    public AlreadyAnsweredException(final String message) {
        super(message);
    }
}
