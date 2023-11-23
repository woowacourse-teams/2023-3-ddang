package com.ddang.ddang.report.application.dto.request;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.report.presentation.dto.request.CreateAnswerReportRequest;
import com.ddang.ddang.user.domain.User;

public record CreateAnswerReportDto(Long answerId, String description, Long reporterId) {

    public static CreateAnswerReportDto of(final CreateAnswerReportRequest createAnswerReportRequest, final Long userId) {
        return new CreateAnswerReportDto(
                createAnswerReportRequest.answerId(),
                createAnswerReportRequest.description(),
                userId
        );
    }

    public AnswerReport toEntity(final Answer answer, final User reporter) {
        return new AnswerReport(reporter, answer, description);
    }
}
