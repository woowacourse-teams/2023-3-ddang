package com.ddang.ddang.review.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInReviewDto(Long id, String name, Long profileImageId, Float reliability, String oauthId) {

    public static ReadUserInReviewDto from(final User user) {
        return new ReadUserInReviewDto(
                user.getId(),
                user.getName(),
                user.getProfileImage().getId(),
                user.getReliability().getValue(),
                user.getOauthId()
        );
    }
}
