package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.report.domain.AnswerReport;
import com.ddang.ddang.user.domain.User;

public record CreateAnswerReportDto(Long answerId, String description, Long reporterId) {

    public AnswerReport toEntity(final Answer answer, final User reporter) {
        return new AnswerReport(reporter, answer, description);
    }
}
