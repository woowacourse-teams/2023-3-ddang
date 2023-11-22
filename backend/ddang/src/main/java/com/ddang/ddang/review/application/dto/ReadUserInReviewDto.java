package com.ddang.ddang.review.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInReviewDto(Long id, String name, String profileImageStoreName, double reliability, String oauthId) {

    public static ReadUserInReviewDto from(final User user) {
        return new ReadUserInReviewDto(
                user.getId(),
                user.findName(),
                user.getProfileImage().getStoreName(),
                user.getReliability().getValue(),
                user.getOauthInformation().getOauthId()
        );
    }
}
