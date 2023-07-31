package com.ddang.ddang.image.infrastructure.local.exception;

public class EmptyImageException extends IllegalArgumentException {

    public EmptyImageException(final String message) {
        super(message);
    }
}
