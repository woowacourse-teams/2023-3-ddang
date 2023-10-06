package com.ddang.ddang.authentication.presentation.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record AccessTokenRequest(@NotEmpty(message = "AccessToken을 입력해주세요.") String accessToken) {
}
