package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.user.domain.User;

public record ReadQnaDto(
        ReadQuestionDto readQuestionDto,
        ReadAnswerDto readAnswerDto
) {

    public static ReadQnaDto of(final Question question, final User user) {
        final ReadQuestionDto readQuestionDto = ReadQuestionDto.of(question, user);
        final ReadAnswerDto readAnswerDto = processReadAnswerDto(question);

        return new ReadQnaDto(readQuestionDto, readAnswerDto);
    }

    private static ReadAnswerDto processReadAnswerDto(final Question question) {
        if (question.getAnswer() == null) {
            return null;
        }

        return ReadAnswerDto.from(question.getAnswer(), question.getAuction().getSeller());
    }
}
