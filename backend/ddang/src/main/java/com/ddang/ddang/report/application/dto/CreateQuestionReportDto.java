package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.report.domain.QuestionReport;
import com.ddang.ddang.report.presentation.dto.request.CreateQuestionReportRequest;
import com.ddang.ddang.user.domain.User;

public record CreateQuestionReportDto(Long questionId, String description, Long reporterId) {

    public static CreateQuestionReportDto of(final CreateQuestionReportRequest questionReportRequest, final Long userId) {
        return new CreateQuestionReportDto(
                questionReportRequest.questionId(),
                questionReportRequest.description(),
                userId
        );
    }

    public QuestionReport toEntity(final Question question, final User reporter) {
        return new QuestionReport(reporter, question, description);
    }
}
