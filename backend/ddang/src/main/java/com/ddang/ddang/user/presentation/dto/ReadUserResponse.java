package com.ddang.ddang.user.presentation.dto;

import com.ddang.ddang.user.application.dto.ReadUserDto;

public record ReadUserResponse(String name, String profileImage, double reliability) {

    public static ReadUserResponse from(final ReadUserDto readUserDto) {
        return new ReadUserResponse(readUserDto.name(), readUserDto.profileImage(), readUserDto.reliability());
    }
}
