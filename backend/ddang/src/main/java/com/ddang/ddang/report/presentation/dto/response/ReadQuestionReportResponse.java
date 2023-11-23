package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadQuestionReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadQuestionReportResponse(
        Long id,

        ReadReporterResponse reporter,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        ReadReportTargetQuestionSimpleInfoResponse question,

        String description
) {

    public static ReadQuestionReportResponse from(final ReadQuestionReportDto readQuestionReportDto) {
        return new ReadQuestionReportResponse(
                readQuestionReportDto.id(),
                new ReadReporterResponse(
                        readQuestionReportDto.reporterDto().id(),
                        readQuestionReportDto.reporterDto().name()
                ),
                readQuestionReportDto.createdTime(),
                new ReadReportTargetQuestionSimpleInfoResponse(readQuestionReportDto.questionDto().id()),
                readQuestionReportDto.description()
        );
    }

    public record ReadReporterResponse(Long id, String name) {
    }

    public record ReadReportTargetQuestionSimpleInfoResponse(Long id) {
    }
}
