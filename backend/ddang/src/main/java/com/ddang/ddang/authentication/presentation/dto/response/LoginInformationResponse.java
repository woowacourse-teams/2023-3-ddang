package com.ddang.ddang.authentication.presentation.dto.response;

import com.ddang.ddang.authentication.application.dto.LoginInformationDto;

public record LoginInformationResponse(String accessToken, String refreshToken, boolean isSignUpUser) {

    public static LoginInformationResponse from(final LoginInformationDto dto) {
        return new LoginInformationResponse(dto.accessToken(), dto.refreshToken(), dto.isSignUpUser());
    }
}
