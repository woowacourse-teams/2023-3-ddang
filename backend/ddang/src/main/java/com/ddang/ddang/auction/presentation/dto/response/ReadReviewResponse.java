package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.review.application.dto.response.ReadSingleReviewDto;
import jakarta.annotation.Nullable;

public record ReadReviewResponse(float score, @Nullable String content) {

    public static ReadReviewResponse from(final ReadSingleReviewDto dto) {
        return new ReadReviewResponse(dto.reviewScore(), dto.content());
    }
}
