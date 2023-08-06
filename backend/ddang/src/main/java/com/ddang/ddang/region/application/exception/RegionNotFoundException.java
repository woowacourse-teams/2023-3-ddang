package com.ddang.ddang.region.application.exception;

public class RegionNotFoundException extends IllegalArgumentException {

    public RegionNotFoundException(final String message) {
        super(message);
    }
}
