package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadQnaDto;
import com.ddang.ddang.qna.application.dto.ReadQnasDto;

import java.util.List;

public record ReadQnasResponse(List<ReadQnaResponse> qnas) {

    public static ReadQnasResponse from(final ReadQnasDto readQnasDto) {
        final List<ReadQnaDto> dtos = readQnasDto.readQnaDtos();
        final List<ReadQnaResponse> readQnaResponses = dtos.stream()
                                                           .map(ReadQnaResponse::from)
                                                           .toList();

        return new ReadQnasResponse(readQnaResponses);
    }
}
