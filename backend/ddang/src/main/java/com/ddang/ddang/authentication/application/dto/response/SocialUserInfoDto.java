package com.ddang.ddang.authentication.application.dto.response;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;

public record SocialUserInfoDto(String id) {

    public static SocialUserInfoDto from(final UserInformationDto dto) {
        return new SocialUserInfoDto(dto.findUserId());
    }
}
