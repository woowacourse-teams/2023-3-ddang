package com.ddang.ddang.qna.application.dto;

import com.ddang.ddang.qna.domain.Question;

import java.util.List;

public record ReadQnasDto(List<ReadQnaDto> readQnaDtos) {

    public static ReadQnasDto from(final List<Question> questions) {
        final List<ReadQnaDto> readQnaDtos = questions.stream()
                                                      .map(ReadQnaDto::from)
                                                      .toList();

        return new ReadQnasDto(readQnaDtos);
    }
}
