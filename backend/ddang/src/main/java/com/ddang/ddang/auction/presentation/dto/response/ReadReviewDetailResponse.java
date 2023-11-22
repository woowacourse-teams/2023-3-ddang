package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.review.application.dto.ReadReviewDetailDto;
import jakarta.annotation.Nullable;

public record ReadReviewDetailResponse(float score, @Nullable String content) {

    public static ReadReviewDetailResponse from(final ReadReviewDetailDto dto) {
        return new ReadReviewDetailResponse(dto.reviewScore(), dto.content());
    }
}
