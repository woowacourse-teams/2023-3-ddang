package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.questionandanswer.domain.Answer;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateAnswerRequest;

public record CreateAnswerDto(Long questionId, String content, Long userId) {

    public static CreateAnswerDto of(final Long questionId, final CreateAnswerRequest answerRequest, final Long userId) {
        return new CreateAnswerDto(questionId, answerRequest.content(), userId);
    }

    public Answer toEntity() {
        return new Answer(content);
    }
}
