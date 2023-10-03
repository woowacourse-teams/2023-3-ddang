package com.ddang.ddang.questionandanswer.application.dto;

import com.ddang.ddang.questionandanswer.domain.Answer;

public record CreateAnswerDto(Long questionId, String content, Long userId) {

    public Answer toEntity() {
        return new Answer(content);
    }
}
