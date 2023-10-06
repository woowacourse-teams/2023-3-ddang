package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.qna.domain.Question;

import java.time.LocalDateTime;

public record ReadQuestionInReportDto(
        Long id,
        ReadUserInReportDto userDto,
        String content,
        LocalDateTime createdTime
) {

    public static ReadQuestionInReportDto from(final Question question) {
        return new ReadQuestionInReportDto(
                question.getId(),
                ReadUserInReportDto.from(question.getWriter()),
                question.getContent(),
                question.getCreatedTime()
        );
    }
}
