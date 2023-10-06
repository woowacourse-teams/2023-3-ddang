package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadAnswerInReportDto;

public record ReadAnswerInReportResponse(Long id) {

    public static ReadAnswerInReportResponse from(final ReadAnswerInReportDto answerDto) {
        return new ReadAnswerInReportResponse(answerDto.id());
    }
}
