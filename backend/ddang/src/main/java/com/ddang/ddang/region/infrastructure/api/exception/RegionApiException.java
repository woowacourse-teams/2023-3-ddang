package com.ddang.ddang.region.infrastructure.api.exception;

public class RegionApiException extends IllegalStateException {

    public RegionApiException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
