package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadAnswerReportDto;

import java.util.List;

public record ReadAnswerReportsResponse(List<ReadAnswerReportResponse> reports) {

    public static ReadAnswerReportsResponse from(final List<ReadAnswerReportDto> readAnswerReportDtos) {
        final List<ReadAnswerReportResponse> reportResponses = readAnswerReportDtos.stream()
                                                                                   .map(ReadAnswerReportResponse::from)
                                                                                   .toList();

        return new ReadAnswerReportsResponse(reportResponses);
    }
}
