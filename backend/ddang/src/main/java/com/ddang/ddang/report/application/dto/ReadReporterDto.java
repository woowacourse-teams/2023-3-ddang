package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadReporterDto(Long id, String name, String profileImage, double reliability) {

    public static ReadReporterDto from(final User reporter) {
        return new ReadReporterDto(
                reporter.getId(),
                reporter.getName(),
                reporter.getProfileImage(),
                reporter.getReliability()
        );
    }
}
