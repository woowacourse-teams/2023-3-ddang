package com.ddang.ddang.review.presentation.dto.response;

import com.ddang.ddang.review.application.dto.response.ReadSingleReviewDto;
import jakarta.annotation.Nullable;

public record ReadSingleReviewResponse(@Nullable Float score, @Nullable String content) {

    public static ReadSingleReviewResponse from(final ReadSingleReviewDto readSingleReviewDto) {
        final Double score = readSingleReviewDto.score();

        if (score == null) {
            return new ReadSingleReviewResponse(null, readSingleReviewDto.content());
        }

        return new ReadSingleReviewResponse(score.floatValue(), readSingleReviewDto.content());
    }
}
