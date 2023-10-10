package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadAnswerReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadAnswerReportResponse(
        Long id,

        ReadReporterResponse reporter,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        ReadAnswerInReportResponse answer,

        String description
) {

    public static ReadAnswerReportResponse from(final ReadAnswerReportDto readAnswerReportDto) {
        return new ReadAnswerReportResponse(
                readAnswerReportDto.id(),
                ReadReporterResponse.from(readAnswerReportDto.reporterDto()),
                readAnswerReportDto.createdTime(),
                ReadAnswerInReportResponse.from(readAnswerReportDto.answerDto()),
                readAnswerReportDto.description()
        );
    }
}
