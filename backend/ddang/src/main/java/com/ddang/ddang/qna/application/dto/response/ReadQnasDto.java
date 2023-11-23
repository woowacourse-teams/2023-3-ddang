package com.ddang.ddang.qna.application.dto.response;

import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.user.domain.User;

import java.util.List;

public record ReadQnasDto(List<ReadQnaDto> readQnaDtos) {

    public static ReadQnasDto of(final List<Question> questions, final User user) {
        final List<ReadQnaDto> readQnaDtos = questions.stream()
                                                      .map(question -> ReadQnaDto.of(question, user))
                                                      .toList();

        return new ReadQnasDto(readQnaDtos);
    }
}
