package com.ddang.ddang.authentication.presentation.dto.response;

import com.ddang.ddang.authentication.application.dto.TokenDto;

public record TokenResponse(String accessToken, String refreshToken) {

    public static TokenResponse from(final TokenDto dto) {
        return new TokenResponse(dto.accessToken(), dto.refreshToken());
    }
}
