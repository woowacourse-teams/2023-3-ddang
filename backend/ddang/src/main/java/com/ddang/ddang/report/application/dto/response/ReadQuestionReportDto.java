package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.report.domain.QuestionReport;

import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadQuestionReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadReportTargetQuestionInfoDto questionDto,
        String description
) {

    public static ReadQuestionReportDto from(final QuestionReport questionReport) {
        return new ReadQuestionReportDto(
                questionReport.getId(),
                ReadReporterDto.from(questionReport.getReporter()),
                questionReport.getCreatedTime(),
                ReadReportTargetQuestionInfoDto.from(questionReport.getQuestion()),
                questionReport.getDescription()
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

    public record ReadReportTargetQuestionInfoDto(
            Long id,
            ReadQuestionWriterInfoDto userDto,
            String content,
            LocalDateTime createdTime
    ) {

        public static ReadReportTargetQuestionInfoDto from(final Question question) {
            return new ReadReportTargetQuestionInfoDto(
                    question.getId(),
                    ReadQuestionWriterInfoDto.from(question.getWriter()),
                    question.getContent(),
                    question.getCreatedTime()
            );
        }

        public record ReadQuestionWriterInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            private static ReadQuestionWriterInfoDto from(final User user) {
                return new ReadQuestionWriterInfoDto(
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
