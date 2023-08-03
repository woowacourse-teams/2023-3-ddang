package com.ddang.ddang.image.infrastructure.local.exception;

public class StoreImageFailureException extends IllegalStateException {

    public StoreImageFailureException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
