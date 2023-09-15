package com.ddang.ddang.user.presentation.dto;

import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadUserResponse(String name, String profileImage, double reliability) {

    public static ReadUserResponse from(final ReadUserDto readUserDto) {
        final String name = NameProcessor.process(readUserDto.isDeleted(), readUserDto.name());
        return new ReadUserResponse(name, readUserDto.profileImage(), readUserDto.reliability());
    }
}
