package com.ddang.ddang.image.application.exception;

public class ImageNotFoundException extends IllegalArgumentException {

    public ImageNotFoundException(final String message) {
        super(message);
    }
}
