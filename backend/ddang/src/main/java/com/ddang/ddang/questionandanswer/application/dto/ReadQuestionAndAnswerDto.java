package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.questionandanswer.domain.Question;

public record ReadQuestionAndAnswerDto(
        ReadQuestionDto readQuestionDto,
        ReadAnswerDto readAnswerDto
) {

    public static ReadQuestionAndAnswerDto from(final Question question) {
        final ReadQuestionDto readQuestionDto = ReadQuestionDto.from(question);
        final ReadAnswerDto readAnswerDto = processReadAnswerDto(question);

        return new ReadQuestionAndAnswerDto(readQuestionDto, readAnswerDto);
    }

    private static ReadAnswerDto processReadAnswerDto(final Question question) {
        if (question.getAnswer() == null) {
            return null;
        }

        return ReadAnswerDto.from(question.getAnswer(), question.getAuction().getSeller());
    }
}
