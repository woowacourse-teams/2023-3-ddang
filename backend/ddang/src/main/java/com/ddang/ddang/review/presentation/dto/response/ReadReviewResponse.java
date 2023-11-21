package com.ddang.ddang.review.presentation.dto.response;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.review.application.dto.ReadReviewDto;
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

    public static ReadReviewResponse of(final ReadReviewDto reviewDto, final ImageRelativeUrl imageRelativeUrl) {
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
}
