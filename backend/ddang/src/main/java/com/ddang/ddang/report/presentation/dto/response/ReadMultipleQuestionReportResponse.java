package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadQuestionReportDto;
import java.time.LocalDateTime;
import java.util.List;

public record ReadMultipleQuestionReportResponse(List<ReadQuestionReportResponse> reports) {

    public static ReadMultipleQuestionReportResponse from(final List<ReadQuestionReportDto> questionReportDtos) {
        final List<ReadQuestionReportResponse> reportsResponse = questionReportDtos.stream()
                                                                                   .map(ReadQuestionReportResponse::from)
                                                                                   .toList();

        return new ReadMultipleQuestionReportResponse(reportsResponse);
    }

    public record ReadQuestionReportResponse(
            Long id,
            ReadReporterResponse reporter,
            LocalDateTime createdTime,
            ReadReportTargetQuestionSimpleInfoResponse question,
            String description
    ) {

        private static ReadQuestionReportResponse from(final ReadQuestionReportDto readQuestionReportDto) {
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
}
