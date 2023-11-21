package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInReportDto(
        Long id,
        String name,
        String profileImageStoreName,
        double reliability,
        String oauthId,
        boolean isSellerDeleted
) {

    public static ReadUserInReportDto from(final User user) {
        return new ReadUserInReportDto(
                user.getId(),
                user.getName(),
                user.getProfileImageStoreName(),
                user.getReliability().getValue(),
                user.getOauthInformation().getOauthId(),
                user.isDeleted()
        );
    }
}
