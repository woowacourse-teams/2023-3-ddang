package com.ddang.ddang.user.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserDto(Long id, String name, String profileImage, double reliability, String oauthId) {

    public static ReadUserDto from(final User user) {
        return new ReadUserDto(
                user.getId(),
                user.getName(),
                user.getProfileImage(),
                user.getReliability(),
                user.getOauthId()
        );
    }
}
