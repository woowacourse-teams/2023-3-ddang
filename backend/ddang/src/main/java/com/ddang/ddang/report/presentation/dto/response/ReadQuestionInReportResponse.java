package com.ddang.ddang.report.presentation.dto.response;

public record ReadQuestionInReportResponse(Long id) {

    public static ReadQuestionInReportResponse from(final Long id) {
        return new ReadQuestionInReportResponse(id);
    }
}
