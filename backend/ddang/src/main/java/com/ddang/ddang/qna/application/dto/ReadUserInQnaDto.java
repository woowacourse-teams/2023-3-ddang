package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInQnaDto(
        Long id,
        String name,
        Long profileImageId,
        double reliability,
        String oauthId,
        boolean isDeleted
) {
    public static ReadUserInQnaDto from(final User writer) {
        return new ReadUserInQnaDto(
                writer.getId(),
                writer.getName(),
                writer.getProfileImage().getId(),
                writer.getReliability().getValue(),
                writer.getOauthInformation().getOauthId(),
                writer.isDeleted()
        );
    }
}
