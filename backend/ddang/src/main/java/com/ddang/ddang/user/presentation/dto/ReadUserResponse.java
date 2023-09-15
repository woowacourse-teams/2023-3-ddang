package com.ddang.ddang.user.presentation.dto;

import com.ddang.ddang.image.presentation.util.ImageBaseUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.application.dto.ReadUserDto;

public record ReadUserResponse(String name, String profileImage, double reliability) {

    public static ReadUserResponse from(final ReadUserDto readUserDto) {
        return new ReadUserResponse(
                readUserDto.name(),
                convertImageUrl(readUserDto.profileImageId()),
                readUserDto.reliability()
        );
    }

    private static String convertImageUrl(final Long id) {
        return ImageUrlCalculator.calculate(ImageBaseUrl.USER, id);
    }
}
