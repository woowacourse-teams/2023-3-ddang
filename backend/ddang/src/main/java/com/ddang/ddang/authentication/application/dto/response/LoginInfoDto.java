package com.ddang.ddang.authentication.application.dto.response;

public record LoginInfoDto(String accessToken, String refreshToken, boolean isSignUpUser) {

    public static LoginInfoDto of(
            final TokenDto tokenDto,
            final LoginUserInfoDto loginUserInfoDto
    ) {
        return new LoginInfoDto(
                tokenDto.accessToken(),
                tokenDto.refreshToken(),
                loginUserInfoDto.persisted()
        );
    }
}
