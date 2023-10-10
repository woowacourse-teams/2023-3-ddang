package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.qna.domain.Answer;

import java.time.LocalDateTime;

public record ReadAnswerInReportDto(
        Long id,
        ReadUserInReportDto userDto,
        String content,
        LocalDateTime createdTime
) {

    public static ReadAnswerInReportDto from(final Answer answer) {
        return new ReadAnswerInReportDto(
                answer.getId(),
                ReadUserInReportDto.from(answer.getWriter()),
                answer.getContent(),
                answer.getCreatedTime()
        );
    }
}
