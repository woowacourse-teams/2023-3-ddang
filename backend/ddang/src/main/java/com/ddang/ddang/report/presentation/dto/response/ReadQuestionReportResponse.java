package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadQuestionReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadQuestionReportResponse(
        Long id,

        ReadReporterResponse reporter,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        ReadQuestionInReportResponse question,

        String description
) {

    public static ReadQuestionReportResponse from(final ReadQuestionReportDto readQuestionReportDto) {
        return new ReadQuestionReportResponse(
                readQuestionReportDto.id(),
                ReadReporterResponse.from(readQuestionReportDto.reporterDto()),
                readQuestionReportDto.createdTime(),
                ReadQuestionInReportResponse.from(readQuestionReportDto.questionDto().id()),
                readQuestionReportDto.description()
        );
    }
}
