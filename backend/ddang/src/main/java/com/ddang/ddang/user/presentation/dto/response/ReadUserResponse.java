package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;
import com.ddang.ddang.user.presentation.util.ReliabilityProcessor;

public record ReadUserResponse(String name, String profileImage, Float reliability) {

    public static ReadUserResponse of(final ReadUserDto readUserDto, final ImageRelativeUrl imageRelativeUrl) {
        return new ReadUserResponse(
                NameProcessor.process(readUserDto.isDeleted(), readUserDto.name()),
                imageRelativeUrl.calculateAbsoluteUrl() + readUserDto.profileImageStoreName(),
                ReliabilityProcessor.process(readUserDto.reliability())
        );
    }
}
