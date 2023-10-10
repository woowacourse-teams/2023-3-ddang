package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadQuestionReportDto;

import java.util.List;

public record ReadQuestionReportsResponse(List<ReadQuestionReportResponse> reports) {

    public static ReadQuestionReportsResponse from(final List<ReadQuestionReportDto> questionReportDtos) {
        final List<ReadQuestionReportResponse> reportsResponse = questionReportDtos.stream()
                                                                                   .map(ReadQuestionReportResponse::from)
                                                                                   .toList();

        return new ReadQuestionReportsResponse(reportsResponse);
    }
}
