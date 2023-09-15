package com.ddang.ddang.user.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserDto(
        Long id,
        String name,
        Long profileImageId,
        double reliability,
        String oauthId,
        boolean isDeleted
) {

    public static ReadUserDto from(final User user) {
        return new ReadUserDto(
                user.getId(),
                user.getName(),
                user.getProfileImage().getId(),
                user.getReliability(),
                user.getOauthId(),
                user.isDeleted()
        );
    }
}
