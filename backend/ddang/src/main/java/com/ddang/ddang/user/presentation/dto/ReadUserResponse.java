package com.ddang.ddang.user.presentation.dto;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadUserResponse(String name, String profileImage, Float reliability) {

    public static ReadUserResponse from(final ReadUserDto readUserDto) {
        final String name = NameProcessor.process(readUserDto.isDeleted(), readUserDto.name());
        final String profileImageUrl = ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, readUserDto.profileImageStoreName());

        final Float floatReliability = Float.valueOf(String.valueOf(readUserDto.reliability()));

        return new ReadUserResponse(name, profileImageUrl, floatReliability);
    }
}
