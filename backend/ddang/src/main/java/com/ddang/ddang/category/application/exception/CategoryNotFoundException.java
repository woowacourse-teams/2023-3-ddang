package com.ddang.ddang.category.application.exception;

public class CategoryNotFoundException extends IllegalArgumentException {

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
