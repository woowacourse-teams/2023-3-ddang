package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInQuestionAndAnswerDto(
        Long id,
        String name,
        Long profileImageId,
        double reliability,
        String oauthId,
        boolean isDeleted
) {
    public static ReadUserInQuestionAndAnswerDto from(final User writer) {
        return new ReadUserInQuestionAndAnswerDto(
                writer.getId(),
                writer.getName(),
                writer.getProfileImage().getId(),
                writer.getReliability(),
                writer.getOauthId(),
                writer.isDeleted()
        );
    }
}
