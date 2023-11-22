package com.ddang.ddang.user.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserDto(
        Long id,
        String name,
        String profileImageStoreName,
        double reliability,
        String oauthId,
        boolean isDeleted
) {

    public static ReadUserDto from(final User user) {
        return new ReadUserDto(
                user.getId(),
                user.findName(),
                user.getProfileImageStoreName(),
                user.getReliability().getValue(),
                user.getOauthInformation().getOauthId(),
                user.isDeleted()
        );
    }
}
