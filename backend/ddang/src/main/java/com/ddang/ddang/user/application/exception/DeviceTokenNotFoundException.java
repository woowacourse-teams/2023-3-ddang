package com.ddang.ddang.user.application.exception;

public class DeviceTokenNotFoundException extends IllegalArgumentException {

    public DeviceTokenNotFoundException(final String message) {
        super(message);
    }
}
