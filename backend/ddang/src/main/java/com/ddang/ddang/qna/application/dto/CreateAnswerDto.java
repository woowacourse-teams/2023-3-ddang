package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.presentation.dto.request.CreateAnswerRequest;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.user.domain.User;

public record CreateAnswerDto(Long questionId, String content, Long userId) {

    public static CreateAnswerDto of(final Long questionId, final CreateAnswerRequest answerRequest, final Long userId) {
        return new CreateAnswerDto(questionId, answerRequest.content(), userId);
    }

    public Answer toEntity(final User writer) {
        return new Answer(writer, content);
    }
}
