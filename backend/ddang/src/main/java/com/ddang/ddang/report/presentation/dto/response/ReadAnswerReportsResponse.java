package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadAnswerReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadAnswerReportsResponse(List<ReadAnswerReportResponse> reports) {

    public static ReadAnswerReportsResponse from(final List<ReadAnswerReportDto> readAnswerReportDtos) {
        final List<ReadAnswerReportResponse> reportResponses = readAnswerReportDtos.stream()
                                                                                   .map(ReadAnswerReportResponse::from)
                                                                                   .toList();

        return new ReadAnswerReportsResponse(reportResponses);
    }

    public record ReadAnswerReportResponse(
            Long id,

            ReadReporterResponse reporter,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            ReadReportTargetAnswerSimpleInfoResponse answer,

            String description
    ) {

        public static ReadAnswerReportResponse from(final ReadAnswerReportDto readAnswerReportDto) {
            return new ReadAnswerReportResponse(
                    readAnswerReportDto.id(),
                    new ReadReporterResponse(
                            readAnswerReportDto.reporterDto().id(),
                            readAnswerReportDto.reporterDto().name()
                    ),
                    readAnswerReportDto.createdTime(),
                    new ReadReportTargetAnswerSimpleInfoResponse(readAnswerReportDto.id()),
                    readAnswerReportDto.description()
            );
        }

        public record ReadReporterResponse(Long id, String name) {
        }

        public record ReadReportTargetAnswerSimpleInfoResponse(Long id) {
        }
    }
}
