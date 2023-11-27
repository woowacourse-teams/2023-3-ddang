package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadAnswerReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadMultipleAnswerReportResponse(List<AnswerReportInfoResponse> reports) {

    public static ReadMultipleAnswerReportResponse from(final List<ReadAnswerReportDto> readAnswerReportDtos) {
        final List<AnswerReportInfoResponse> reportResponses = readAnswerReportDtos.stream()
                                                                                   .map(AnswerReportInfoResponse::from)
                                                                                   .toList();

        return new ReadMultipleAnswerReportResponse(reportResponses);
    }

    public record AnswerReportInfoResponse(
            Long id,

            ReporterInfoResponse reporter,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            ReportedAnswerInfoResponse answer,

            String description
    ) {

        public static AnswerReportInfoResponse from(final ReadAnswerReportDto readAnswerReportDto) {
            return new AnswerReportInfoResponse(
                    readAnswerReportDto.id(),
                    new ReporterInfoResponse(
                            readAnswerReportDto.reporterDto().id(),
                            readAnswerReportDto.reporterDto().name()
                    ),
                    readAnswerReportDto.createdTime(),
                    new ReportedAnswerInfoResponse(readAnswerReportDto.id()),
                    readAnswerReportDto.description()
            );
        }

        public record ReporterInfoResponse(Long id, String name) {
        }

        public record ReportedAnswerInfoResponse(Long id) {
        }
    }
}
