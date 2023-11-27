package com.ddang.ddang.review.presentation.dto.response;

import com.ddang.ddang.review.application.dto.response.ReadMultipleReviewDto;
import com.ddang.ddang.review.application.dto.response.ReadMultipleReviewDto.ReviewerInfoDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadMultipleReviewResponse(
        Long id,

        ReviewerInfoResponse reviewer,

        String content,

        Float score,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime
) {

    public static ReadMultipleReviewResponse of(final ReadMultipleReviewDto reviewDto, final String imageRelativeUrl) {
        final Double nullableScore = reviewDto.score();
        Float returnScore = null;

        if (nullableScore != null) {
            returnScore = nullableScore.floatValue();
        }

        return new ReadMultipleReviewResponse(
                reviewDto.id(),
                ReviewerInfoResponse.of(reviewDto.reviewer(), imageRelativeUrl),
                reviewDto.content(),
                returnScore,
                reviewDto.createdTime()
        );
    }

    public record ReviewerInfoResponse(Long id, String name, String profileImage) {

        public static ReviewerInfoResponse of(final ReviewerInfoDto reviewerDto, final String imageRelativeUrl) {
            return new ReviewerInfoResponse(
                    reviewerDto.id(),
                    reviewerDto.name(),
                    imageRelativeUrl + reviewerDto.profileImageStoreName()
            );
        }
    }
}
