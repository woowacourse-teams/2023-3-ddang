package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.image.application.util.ImageIdProcessor;
import com.ddang.ddang.user.domain.User;

public record ReadUserInReportDto(
        Long id,
        String name,
        Long profileImageId,
        double reliability,
        String oauthId,
        boolean isSellerDeleted
) {

    public static ReadUserInReportDto from(final User user) {
        return new ReadUserInReportDto(
                user.getId(),
                user.getName(),
                ImageIdProcessor.process(user.getProfileImage()),
                user.getReliability(),
                user.getOauthId(),
                user.isDeleted()
        );
    }
}
