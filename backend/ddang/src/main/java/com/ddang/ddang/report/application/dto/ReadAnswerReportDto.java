package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.report.domain.AnswerReport;

import java.time.LocalDateTime;

public record ReadAnswerReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadAnswerInReportDto answerDto,
        String description
) {

    public static ReadAnswerReportDto from(final AnswerReport answerReport) {
        return new ReadAnswerReportDto(
                answerReport.getId(),
                ReadReporterDto.from(answerReport.getReporter()),
                answerReport.getCreatedTime(),
                ReadAnswerInReportDto.from(answerReport.getAnswer()),
                answerReport.getDescription()
        );
    }
}
