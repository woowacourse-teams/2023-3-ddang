package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.ReadQuestionAndAnswersDto;

import java.util.List;

public record ReadQuestionAndAnswersResponse(List<ReadQuestionAndAnswerResponse> questionAndAnswers) {

    public static ReadQuestionAndAnswersResponse from(final ReadQuestionAndAnswersDto readQuestionAndAnswersDto) {
        final List<ReadQuestionAndAnswerDto> dtos = readQuestionAndAnswersDto.readQuestionAndAnswerDtos();
        final List<ReadQuestionAndAnswerResponse> questionAndAnswerDtos = dtos.stream()
                                                                              .map(ReadQuestionAndAnswerResponse::from)
                                                                              .toList();

        return new ReadQuestionAndAnswersResponse(questionAndAnswerDtos);
    }
}
