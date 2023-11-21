package com.ddang.ddang.qna.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadQnaDto;
import com.ddang.ddang.qna.application.dto.ReadQnasDto;

import java.util.List;

public record ReadQnasResponse(List<ReadQnaResponse> qnas) {

    public static ReadQnasResponse of(final ReadQnasDto readQnasDto, final String imageRelativeUrl) {
        final List<ReadQnaDto> dtos = readQnasDto.readQnaDtos();
        final List<ReadQnaResponse> readQnaResponses = dtos.stream()
                                                           .map(dto -> ReadQnaResponse.of(dto, imageRelativeUrl))
                                                           .toList();

        return new ReadQnasResponse(readQnaResponses);
    }
}
