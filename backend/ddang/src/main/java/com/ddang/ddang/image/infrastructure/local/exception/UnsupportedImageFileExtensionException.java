package com.ddang.ddang.image.infrastructure.local.exception;

public class UnsupportedImageFileExtensionException extends IllegalArgumentException {

    public UnsupportedImageFileExtensionException(final String message) {
        super(message);
    }
}
