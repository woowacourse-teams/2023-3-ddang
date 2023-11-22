package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.user.domain.User;

public record ReadReporterDto(Long id, String name, String profileImageStoreName, double reliability, boolean isDeleted) {

    public static ReadReporterDto from(final User reporter) {
        return new ReadReporterDto(
                reporter.getId(),
                reporter.findName(),
                reporter.getProfileImageStoreName(),
                reporter.getReliability().getValue(),
                reporter.isDeleted()
        );
    }
}
