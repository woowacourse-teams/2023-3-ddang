package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.questionandanswer.domain.Question;

import java.time.LocalDateTime;

public record ReadQuestionDto(
        Long id,
        ReadUserInQuestionAndAnswerDto readUserInQuestionAndAnswerDto,
        String content,
        LocalDateTime createdTime
) {

    public static ReadQuestionDto from(final Question question) {
        return new ReadQuestionDto(
                question.getId(),
                ReadUserInQuestionAndAnswerDto.from(question.getWriter()),
                question.getContent(),
                question.getCreatedTime()
        );
    }
}
