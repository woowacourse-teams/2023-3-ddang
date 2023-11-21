package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadQuestionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadQuestionResponse(
        Long id,

        ReadUserInAuctionQuestionResponse writer,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        String content,

        boolean isQuestioner
) {

    public static ReadQuestionResponse of(
            final ReadQuestionDto readQuestionDto,
            final String imageRelativeUrl
    ) {
        return new ReadQuestionResponse(
                readQuestionDto.id(),
                ReadUserInAuctionQuestionResponse.of(readQuestionDto.readUserInQnaDto(), imageRelativeUrl),
                readQuestionDto.createdTime(),
                readQuestionDto.content(),
                readQuestionDto.isQuestioner()
        );
    }
}
