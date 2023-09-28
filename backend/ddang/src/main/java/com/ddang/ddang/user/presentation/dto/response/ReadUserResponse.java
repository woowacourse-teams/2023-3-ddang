package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadUserResponse(String name, String profileImage, double reliability) {

    public static ReadUserResponse from(final ReadUserDto readUserDto) {
        return new ReadUserResponse(
                NameProcessor.process(readUserDto.isDeleted(), readUserDto.name()),
                convertImageFullUrl(readUserDto.profileImageId()),
                readUserDto.reliability()
        );
    }

    private static String convertImageFullUrl(final Long id) {
        return ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, id);
    }
}
