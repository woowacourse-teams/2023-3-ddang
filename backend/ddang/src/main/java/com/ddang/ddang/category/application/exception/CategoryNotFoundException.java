package com.ddang.ddang.category.application.exception;

public class CategoryNotFoundException extends IllegalArgumentException {

    public CategoryNotFoundException(final String message) {
        super(message);
    }
}
