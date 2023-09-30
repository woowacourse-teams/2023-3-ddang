package com.ddang.ddang.review.presentation.response;

import com.ddang.ddang.review.application.dto.ReadReviewDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadReviewResponse(
        Long id,

        ReadUserInReviewResponse writer,

        String content,

        Double score,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime
) {

    public static ReadReviewResponse from(final ReadReviewDto reviewDto) {
        return new ReadReviewResponse(
                reviewDto.id(),
                ReadUserInReviewResponse.from(reviewDto.writer()),
                reviewDto.content(),
                reviewDto.score(),
                reviewDto.createdTime()
        );
    }
}
