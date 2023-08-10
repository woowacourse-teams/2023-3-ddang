package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadUserInReportDto(Long id, String name, String profileImage, double reliability) {

    public static ReadUserInReportDto from(final User seller) {
        return new ReadUserInReportDto(
                seller.getId(),
                seller.getName(),
                seller.getProfileImage(),
                seller.getReliability()
        );
    }
}
