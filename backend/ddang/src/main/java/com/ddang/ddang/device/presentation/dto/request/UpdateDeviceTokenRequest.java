package com.ddang.ddang.device.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record UpdateDeviceTokenRequest(@NotEmpty(message = "기기 토큰이 입력되지 않았습니다.") String deviceToken) {
}
