package com.ddang.ddang.review.application.dto.response;

import com.ddang.ddang.review.domain.Review;

import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadMultipleReviewDto(
        Long id,
        ReviewerInfoDto reviewer,
        String content,
        Double score,
        LocalDateTime createdTime
) {

    public static ReadMultipleReviewDto from(final Review review) {
        return new ReadMultipleReviewDto(
                review.getId(),
                ReviewerInfoDto.from(review.getWriter()),
                review.getContent(),
                review.getScore().getValue(),
                review.getCreatedTime()
        );
    }

    public record ReviewerInfoDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            String oauthId
    ) {

        public static ReviewerInfoDto from(final User user) {
            return new ReviewerInfoDto(
                    user.getId(),
                    user.findName(),
                    user.getProfileImage().getStoreName(),
                    user.getReliability().getValue(),
                    user.getOauthInformation().getOauthId()
            );
        }
    }
}
