package com.ddang.ddang.user.application.dto;

import com.ddang.ddang.image.application.util.ImageIdProcessor;
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
                ImageIdProcessor.process(user.getProfileImage()),
                user.getReliability().getValue(),
                user.getOauthId(),
                user.isDeleted()
        );
    }
}
