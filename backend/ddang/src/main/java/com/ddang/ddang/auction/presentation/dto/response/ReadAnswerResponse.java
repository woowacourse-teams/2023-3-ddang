package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadAnswerDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadAnswerResponse(
        Long id,

        ReadUserInAuctionQuestionResponse writer,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        String content
) {

    public static ReadAnswerResponse of(final ReadAnswerDto readAnswerDto, final String imageRelativeUrl) {
        return new ReadAnswerResponse(
                readAnswerDto.id(),
                ReadUserInAuctionQuestionResponse.of(readAnswerDto.writerDto(), imageRelativeUrl),
                readAnswerDto.createdTime(),
                readAnswerDto.content()
        );
    }
}
