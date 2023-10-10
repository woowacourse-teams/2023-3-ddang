package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.domain.Question;

import java.time.LocalDateTime;

public record ReadQuestionDto(
        Long id,
        ReadUserInQnaDto readUserInQnaDto,
        String content,
        LocalDateTime createdTime
) {

    public static ReadQuestionDto from(final Question question) {
        return new ReadQuestionDto(
                question.getId(),
                ReadUserInQnaDto.from(question.getWriter()),
                question.getContent(),
                question.getCreatedTime()
        );
    }
}
