package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadAnswerDto;
import com.ddang.ddang.qna.application.dto.ReadQnaDto;

public record ReadQnaResponse(
        ReadQuestionResponse question,
        ReadAnswerResponse answer
) {

    public static ReadQnaResponse from(final ReadQnaDto readQnaDto) {
        final ReadQuestionResponse question = ReadQuestionResponse.from(readQnaDto.readQuestionDto());
        final ReadAnswerResponse answer = processReadAnswerResponse(readQnaDto.readAnswerDto());

        return new ReadQnaResponse(question, answer);
    }

    private static ReadAnswerResponse processReadAnswerResponse(final ReadAnswerDto readAnswerDto) {
        if (readAnswerDto == null || readAnswerDto.isDeleted()) {
            return null;
        }

        return ReadAnswerResponse.from(readAnswerDto);
    }
}
