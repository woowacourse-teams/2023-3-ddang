package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserDto(Long id, String name, String profileImage, double reliability) {

    public static ReadUserDto from(final User user) {
        return new ReadUserDto(user.getId(), user.getName(), user.getProfileImage(), user.getReliability());
    }
}
