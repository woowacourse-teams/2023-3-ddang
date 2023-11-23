package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadAnswerReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadReportTargetAnswerInfoDto answerDto,
        String description
) {

    public static ReadAnswerReportDto from(final AnswerReport answerReport) {
        return new ReadAnswerReportDto(
                answerReport.getId(),
                ReadReporterDto.from(answerReport.getReporter()),
                answerReport.getCreatedTime(),
                ReadReportTargetAnswerInfoDto.from(answerReport.getAnswer()),
                answerReport.getDescription()
        );
    }

    public record ReadReporterDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        private static ReadReporterDto from(final User reporter) {
            return new ReadReporterDto(
                    reporter.getId(),
                    reporter.findName(),
                    reporter.getProfileImageStoreName(),
                    reporter.getReliability().getValue(),
                    reporter.isDeleted()
            );
        }
    }

    public record ReadReportTargetAnswerInfoDto(
            Long id,
            ReadAnswerWriterInfoDto userDto,
            String content,
            LocalDateTime createdTime
    ) {

        private static ReadReportTargetAnswerInfoDto from(final Answer answer) {
            return new ReadReportTargetAnswerInfoDto(
                    answer.getId(),
                    ReadAnswerWriterInfoDto.from(answer.getWriter()),
                    answer.getContent(),
                    answer.getCreatedTime()
            );
        }

        public record ReadAnswerWriterInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            private static ReadAnswerWriterInfoDto from(final User user) {
                return new ReadAnswerWriterInfoDto(
                        user.getId(),
                        user.findName(),
                        user.getProfileImageStoreName(),
                        user.getReliability().getValue(),
                        user.getOauthInformation().getOauthId(),
                        user.isDeleted()
                );
            }
        }
    }
}
