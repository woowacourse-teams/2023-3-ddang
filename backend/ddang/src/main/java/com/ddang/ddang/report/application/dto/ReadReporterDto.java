package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.image.application.util.ImageIdProcessor;
import com.ddang.ddang.user.domain.User;

public record ReadReporterDto(Long id, String name, Long profileImageId, Double reliability, boolean isDeleted) {

    public static ReadReporterDto from(final User reporter) {
        return new ReadReporterDto(
                reporter.getId(),
                reporter.getName(),
                ImageIdProcessor.process(reporter.getProfileImage()),
                reporter.getReliability(),
                reporter.isDeleted()
        );
    }
}
