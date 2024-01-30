package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.image.application.util.ImageStoreNameProcessor;
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
                writer.getName(),
                ImageStoreNameProcessor.process(writer.getProfileImage()),
                writer.getReliability().getValue(),
                writer.getOauthInformation().getOauthId(),
                writer.isDeleted()
        );
    }
}
