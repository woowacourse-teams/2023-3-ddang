package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.report.domain.QuestionReport;

import java.time.LocalDateTime;

public record ReadQuestionReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadQuestionInReportDto questionDto,
        String description
) {

    public static ReadQuestionReportDto from(final QuestionReport questionReport) {
        return new ReadQuestionReportDto(
                questionReport.getId(),
                ReadReporterDto.from(questionReport.getReporter()),
                questionReport.getCreatedTime(),
                ReadQuestionInReportDto.from(questionReport.getQuestion()),
                questionReport.getDescription()
        );
    }
}
