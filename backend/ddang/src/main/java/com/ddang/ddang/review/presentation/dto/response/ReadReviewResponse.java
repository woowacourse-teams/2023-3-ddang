package com.ddang.ddang.review.presentation.dto.response;

import com.ddang.ddang.review.application.dto.response.ReadReviewDto;
import com.ddang.ddang.review.application.dto.response.ReadUserInReviewDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadReviewResponse(
        Long id,

        ReadUserInReviewResponse writer,

        String content,

        Float score,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime
) {

    public static ReadReviewResponse of(final ReadReviewDto reviewDto, final String imageRelativeUrl) {
        final Double nullableScore = reviewDto.score();
        Float returnScore = null;

        if (nullableScore != null) {
            returnScore = nullableScore.floatValue();
        }

        return new ReadReviewResponse(
                reviewDto.id(),
                ReadUserInReviewResponse.of(reviewDto.writer(), imageRelativeUrl),
                reviewDto.content(),
                returnScore,
                reviewDto.createdTime()
        );
    }

    public record ReadUserInReviewResponse(Long id, String name, String profileImage) {

        public static ReadUserInReviewResponse of(final ReadUserInReviewDto userDto, final String imageRelativeUrl) {
            return new ReadUserInReviewResponse(
                    userDto.id(),
                    userDto.name(),
                    imageRelativeUrl + userDto.profileImageStoreName()
            );
        }
    }

}
