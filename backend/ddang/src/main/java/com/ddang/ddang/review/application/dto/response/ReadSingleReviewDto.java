package com.ddang.ddang.review.application.dto.response;

import com.ddang.ddang.review.domain.Review;

import javax.annotation.Nullable;

public record ReadSingleReviewDto(@Nullable Double score, @Nullable String content) {

    private static final Double EMPTY_SCORE = null;
    private static final String EMPTY_CONTENT = null;

    public static final ReadSingleReviewDto EMPTY = new ReadSingleReviewDto(EMPTY_SCORE, EMPTY_CONTENT);

    public static ReadSingleReviewDto from(final Review review) {
        return new ReadSingleReviewDto(review.getScore().getValue(), review.getContent());
    }

    public float reviewScore() {
        if (score == null) {
            return 0.0f;
        }

        return score.floatValue();
    }
}
