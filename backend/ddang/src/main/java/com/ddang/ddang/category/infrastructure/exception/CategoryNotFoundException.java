package com.ddang.ddang.category.infrastructure.exception;

public class CategoryNotFoundException extends IllegalArgumentException {

    public CategoryNotFoundException(final String message) {
        super(message);
    }
}
