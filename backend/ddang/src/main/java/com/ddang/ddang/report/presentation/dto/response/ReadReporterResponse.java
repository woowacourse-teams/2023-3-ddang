package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadReporterDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadReporterResponse(Long id, String name) {

    public static ReadReporterResponse from(final ReadReporterDto reporterDto) {
        final String name = NameProcessor.process(reporterDto.isDeleted(), reporterDto.name());

        return new ReadReporterResponse(reporterDto.id(), name);
    }
}
