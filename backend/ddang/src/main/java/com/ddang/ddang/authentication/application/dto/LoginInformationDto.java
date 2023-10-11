package com.ddang.ddang.authentication.application.dto;

public record LoginInformationDto(TokenDto tokenDto, boolean persisted) {

    public static LoginInformationDto of(
            final TokenDto tokenDto,
            final LoginUserInformationDto loginUserInformationDto
    ) {
        return new LoginInformationDto(tokenDto, loginUserInformationDto.persisted());
    }
}
