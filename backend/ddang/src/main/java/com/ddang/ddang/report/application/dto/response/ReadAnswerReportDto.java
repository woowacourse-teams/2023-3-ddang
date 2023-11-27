package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadAnswerReportDto(
        Long id,
        ReporterInfoDto reporterDto,
        LocalDateTime createdTime,
        ReportedAnswerInfoDto answerDto,
        String description
) {

    public static ReadAnswerReportDto from(final AnswerReport answerReport) {
        return new ReadAnswerReportDto(
                answerReport.getId(),
                ReporterInfoDto.from(answerReport.getReporter()),
                answerReport.getCreatedTime(),
                ReportedAnswerInfoDto.from(answerReport.getAnswer()),
                answerReport.getDescription()
        );
    }

    public record ReporterInfoDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        public static ReporterInfoDto from(final User reporter) {
            return new ReporterInfoDto(
                    reporter.getId(),
                    reporter.findName(),
                    reporter.getProfileImageStoreName(),
                    reporter.getReliability().getValue(),
                    reporter.isDeleted()
            );
        }
    }

    public record ReportedAnswerInfoDto(
            Long id,
            AnswererInfoDto userDto,
            String content,
            LocalDateTime createdTime
    ) {

        public static ReportedAnswerInfoDto from(final Answer answer) {
            return new ReportedAnswerInfoDto(
                    answer.getId(),
                    AnswererInfoDto.from(answer.getWriter()),
                    answer.getContent(),
                    answer.getCreatedTime()
            );
        }

        public record AnswererInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            public static AnswererInfoDto from(final User user) {
                return new AnswererInfoDto(
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
