package com.ddang.ddang.authentication.application.dto;

public record LoginInformationDto(String accessToken, String refreshToken, boolean isSignUpUser) {

    public static LoginInformationDto of(
            final TokenDto tokenDto,
            final LoginUserInformationDto loginUserInformationDto
    ) {
        return new LoginInformationDto(
                tokenDto.accessToken(),
                tokenDto.refreshToken(),
                loginUserInformationDto.persisted()
        );
    }
}
