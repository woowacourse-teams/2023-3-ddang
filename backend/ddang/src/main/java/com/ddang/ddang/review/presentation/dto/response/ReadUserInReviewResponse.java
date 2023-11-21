package com.ddang.ddang.review.presentation.dto.response;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.review.application.dto.ReadUserInReviewDto;

public record ReadUserInReviewResponse(Long id, String name, String profileImage) {

    public static ReadUserInReviewResponse from(final ReadUserInReviewDto userDto) {
        return new ReadUserInReviewResponse(
                userDto.id(),
                userDto.name(),
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, userDto.profileImageStoreName())
        );
    }
}
