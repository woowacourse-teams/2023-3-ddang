package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadAnswerDto(
        Long id,
        ReadUserInQnaDto writerDto,
        String content,
        LocalDateTime createdTime
) {
    public static ReadAnswerDto from(final Answer answer, final User writer) {
        return new ReadAnswerDto(
                answer.getId(),
                ReadUserInQnaDto.from(writer),
                answer.getContent(),
                answer.getCreatedTime()
        );
    }
}
