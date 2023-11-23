package com.ddang.ddang.qna.application.dto.response;

import com.ddang.ddang.user.domain.User;

public record ReadUserInQnaDto(
        Long id,
        String name,
        String profileImageStoreName,
        double reliability,
        String oauthId,
        boolean isDeleted
) {
    public static ReadUserInQnaDto from(final User writer) {
        return new ReadUserInQnaDto(
                writer.getId(),
                writer.findName(),
                writer.getProfileImage().getStoreName(),
                writer.getReliability().getValue(),
                writer.getOauthInformation().getOauthId(),
                writer.isDeleted()
        );
    }
}
