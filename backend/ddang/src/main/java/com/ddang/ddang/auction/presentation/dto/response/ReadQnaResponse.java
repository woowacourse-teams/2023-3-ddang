package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadAnswerDto;
import com.ddang.ddang.qna.application.dto.ReadQnaDto;

public record ReadQnaResponse(
        ReadQuestionResponse question,
        ReadAnswerResponse answer
) {

    public static ReadQnaResponse of(final ReadQnaDto readQnaDto, final String imageRelativeUrl) {
        final ReadQuestionResponse question = ReadQuestionResponse.of(readQnaDto.readQuestionDto(), imageRelativeUrl);
        final ReadAnswerResponse answer = processReadAnswerResponse(readQnaDto.readAnswerDto(), imageRelativeUrl);

        return new ReadQnaResponse(question, answer);
    }

    private static ReadAnswerResponse processReadAnswerResponse(
            final ReadAnswerDto readAnswerDto,
            final String imageRelativeUrl
    ) {
        if (readAnswerDto == null || readAnswerDto.isDeleted()) {
            return null;
        }

        return ReadAnswerResponse.of(readAnswerDto, imageRelativeUrl);
    }
}
