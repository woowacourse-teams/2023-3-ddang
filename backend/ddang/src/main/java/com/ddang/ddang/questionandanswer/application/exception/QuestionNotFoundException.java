package com.ddang.ddang.questionandanswer.application.exception;

public class QuestionNotFoundException extends IllegalArgumentException {

    public QuestionNotFoundException(final String message) {
        super(message);
    }
}
