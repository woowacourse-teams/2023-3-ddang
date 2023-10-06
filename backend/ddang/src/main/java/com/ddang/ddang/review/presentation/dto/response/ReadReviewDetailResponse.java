package com.ddang.ddang.review.presentation.dto.response;

import com.ddang.ddang.review.application.dto.ReadReviewDetailDto;
import jakarta.annotation.Nullable;


public record ReadReviewDetailResponse(@Nullable Double score, @Nullable String content) {

    public static ReadReviewDetailResponse from(final ReadReviewDetailDto readReviewDetailDto) {
        return new ReadReviewDetailResponse(readReviewDetailDto.score(), readReviewDetailDto.content());
    }
}
