package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadReporterDto;

public record ReadReporterResponse(Long id, String name) {

    public static ReadReporterResponse from(final ReadReporterDto reporterDto) {
        return new ReadReporterResponse(reporterDto.id(), reporterDto.name());
    }
}
