package com.ddang.ddang.device.application.dto.request;

import com.ddang.ddang.device.presentation.dto.request.UpdateDeviceTokenRequest;

public record CreateDeviceTokenDto(String deviceToken) {

    public static CreateDeviceTokenDto from(final UpdateDeviceTokenRequest updateDeviceTokenRequest) {
        return new CreateDeviceTokenDto(updateDeviceTokenRequest.deviceToken());
    }
}
