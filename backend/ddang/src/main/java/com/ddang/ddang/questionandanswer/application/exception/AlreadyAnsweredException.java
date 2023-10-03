package com.ddang.ddang.questionandanswer.application.exception;

public class AlreadyAnsweredException extends IllegalArgumentException {

    public AlreadyAnsweredException(final String message) {
        super(message);
    }
}
