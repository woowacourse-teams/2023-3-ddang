package com.ddang.ddang.review.application.dto;

import com.ddang.ddang.review.domain.Review;

import javax.annotation.Nullable;

public record ReadReviewDetailDto(@Nullable Double score, @Nullable String content) {

    private static final Double EMPTY_SCORE = null;
    private static final String EMPTY_CONTENT = null;
    public static final ReadReviewDetailDto EMPTY = new ReadReviewDetailDto(EMPTY_SCORE, EMPTY_CONTENT);

    public static ReadReviewDetailDto from(final Review review) {
        return new ReadReviewDetailDto(review.getScore().getValue(), review.getContent());
    }
}
