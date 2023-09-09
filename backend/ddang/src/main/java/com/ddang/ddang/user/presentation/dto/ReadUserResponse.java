package com.ddang.ddang.user.presentation.dto;

import com.ddang.ddang.image.util.ImageBaseUrl;
import com.ddang.ddang.image.util.ImageUrlBuilder;
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
        return ImageUrlBuilder.calculate(ImageBaseUrl.USER, id);
    }
}
