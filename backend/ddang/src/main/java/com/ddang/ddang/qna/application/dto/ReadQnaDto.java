package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.domain.Question;

public record ReadQnaDto(
        ReadQuestionDto readQuestionDto,
        ReadAnswerDto readAnswerDto
) {

    public static ReadQnaDto from(final Question question) {
        final ReadQuestionDto readQuestionDto = ReadQuestionDto.from(question);
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
