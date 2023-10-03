package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.questionandanswer.domain.Answer;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadAnswerDto(
        Long id,
        ReadUserInQuestionAndAnswerDto writerDto,
        String content,
        LocalDateTime createdTime
) {
    public static ReadAnswerDto from(final Answer answer, final User writer) {
        return new ReadAnswerDto(
                answer.getId(),
                ReadUserInQuestionAndAnswerDto.from(writer),
                answer.getContent(),
                answer.getCreatedTime()
        );
    }
}
