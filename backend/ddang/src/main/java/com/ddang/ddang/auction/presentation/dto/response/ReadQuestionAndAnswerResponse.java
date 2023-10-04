package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.questionandanswer.application.dto.ReadAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswerDto;

public record ReadQuestionAndAnswerResponse(
        ReadQuestionResponse question,
        ReadAnswerResponse answer
) {

    public static ReadQuestionAndAnswerResponse from(final ReadQuestionAndAnswerDto readQuestionAndAnswerDto) {
        final ReadQuestionResponse question = ReadQuestionResponse.from(readQuestionAndAnswerDto.readQuestionDto());
        final ReadAnswerResponse answer = processReadAnswerResponse(readQuestionAndAnswerDto.readAnswerDto());

        return new ReadQuestionAndAnswerResponse(question, answer);
    }

    private static ReadAnswerResponse processReadAnswerResponse(final ReadAnswerDto readAnswerDto) {
        if (readAnswerDto == null) {
            return null;
        }

        return ReadAnswerResponse.from(readAnswerDto);
    }
}
