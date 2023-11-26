package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.review.application.dto.response.ReadReviewDetailDto;
import jakarta.annotation.Nullable;

public record ReadReviewResponse(float score, @Nullable String content) {

    public static ReadReviewResponse from(final ReadReviewDetailDto dto) {
        return new ReadReviewResponse(dto.reviewScore(), dto.content());
    }
}
