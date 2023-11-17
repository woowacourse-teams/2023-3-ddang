package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.review.application.dto.ReadReviewDetailDto;
import jakarta.annotation.Nullable;

public record ReadReviewDetailResponse(@Nullable Float score, @Nullable String content) {

    public static ReadReviewDetailResponse from(final ReadReviewDetailDto readReviewDetailDto) {
        final Double nullableScore = readReviewDetailDto.score();
        if (nullableScore == null) {
            return new ReadReviewDetailResponse(null, readReviewDetailDto.content());
        }

        return new ReadReviewDetailResponse(nullableScore.floatValue(), readReviewDetailDto.content());
    }
}
