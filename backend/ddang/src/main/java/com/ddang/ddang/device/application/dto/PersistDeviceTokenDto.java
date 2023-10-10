package com.ddang.ddang.device.application.dto;

import com.ddang.ddang.device.presentation.dto.request.UpdateDeviceTokenRequest;

public record PersistDeviceTokenDto(String deviceToken) {

    public static PersistDeviceTokenDto from(final UpdateDeviceTokenRequest updateDeviceTokenRequest) {
        return new PersistDeviceTokenDto(updateDeviceTokenRequest.deviceToken());
    }
}
