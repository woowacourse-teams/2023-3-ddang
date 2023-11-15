package com.ddang.ddang.authentication.application.dto;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;

public record SocialUserInformationDto(String id) {

    public static SocialUserInformationDto from(final UserInformationDto dto) {
        return new SocialUserInformationDto(dto.findUserId());
    }
}
