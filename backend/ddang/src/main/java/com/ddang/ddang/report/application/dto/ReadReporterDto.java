package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.image.application.util.ImageStoreNameProcessor;
import com.ddang.ddang.user.domain.User;

public record ReadReporterDto(Long id, String name, String profileImageStoreName, double reliability, boolean isDeleted) {

    public static ReadReporterDto from(final User reporter) {
        return new ReadReporterDto(
                reporter.getId(),
                reporter.getName(),
                ImageStoreNameProcessor.process(reporter.getProfileImage()),
                reporter.getReliability().getValue(),
                reporter.isDeleted()
        );
    }
}
