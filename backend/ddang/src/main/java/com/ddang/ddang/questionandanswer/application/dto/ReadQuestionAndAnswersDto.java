package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.questionandanswer.domain.Question;

import java.util.List;

public record ReadQuestionAndAnswersDto(List<ReadQuestionAndAnswerDto> readQuestionAndAnswerDtos) {

    public static ReadQuestionAndAnswersDto from(final List<Question> questions) {
        final List<ReadQuestionAndAnswerDto> questionAndAnswerDtos = questions.stream()
                                                                              .map(ReadQuestionAndAnswerDto::from)
                                                                              .toList();

        return new ReadQuestionAndAnswersDto(questionAndAnswerDtos);
    }
}
