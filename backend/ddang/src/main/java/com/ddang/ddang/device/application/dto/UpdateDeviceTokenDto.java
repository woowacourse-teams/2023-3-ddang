package com.ddang.ddang.device.application.dto;

import com.ddang.ddang.device.presentation.dto.request.UpdateDeviceTokenRequest;

// TODO dto 이름 update로 해도 괜찮은가요? 서비스에서 create랑 update를 한 번에 처리하다보니 뭐로 할지 모르겠네요
public record UpdateDeviceTokenDto(String deviceToken) {
    public static UpdateDeviceTokenDto from(final UpdateDeviceTokenRequest updateDeviceTokenRequest) {
        return new UpdateDeviceTokenDto(updateDeviceTokenRequest.deviceToken());
    }
}
