package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.report.domain.QuestionReport;

import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadQuestionReportDto(
        Long id,
        ReporterInfoDto reporterDto,
        LocalDateTime createdTime,
        ReportedQuestionInfoDto questionDto,
        String description
) {

    public static ReadQuestionReportDto from(final QuestionReport questionReport) {
        return new ReadQuestionReportDto(
                questionReport.getId(),
                ReporterInfoDto.from(questionReport.getReporter()),
                questionReport.getCreatedTime(),
                ReportedQuestionInfoDto.from(questionReport.getQuestion()),
                questionReport.getDescription()
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

    public record ReportedQuestionInfoDto(
            Long id,
            QuestionerInfoDto userDto,
            String content,
            LocalDateTime createdTime
    ) {

        public static ReportedQuestionInfoDto from(final Question question) {
            return new ReportedQuestionInfoDto(
                    question.getId(),
                    QuestionerInfoDto.from(question.getWriter()),
                    question.getContent(),
                    question.getCreatedTime()
            );
        }

        public record QuestionerInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            public static QuestionerInfoDto from(final User user) {
                return new QuestionerInfoDto(
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
