package com.ddang.ddang.review.application.dto;

import com.ddang.ddang.review.domain.Review;

import java.time.LocalDateTime;

public record ReadReviewDto(
        Long id,
        ReadUserInReviewDto writer,
        String content,
        Double score,
        LocalDateTime createdTime
) {

    public static ReadReviewDto from(final Review review) {
        return new ReadReviewDto(
                review.getId(),
                ReadUserInReviewDto.from(review.getWriter()),
                review.getContent(),
                review.getScore(),
                review.getCreatedTime()
        );
    }
}
