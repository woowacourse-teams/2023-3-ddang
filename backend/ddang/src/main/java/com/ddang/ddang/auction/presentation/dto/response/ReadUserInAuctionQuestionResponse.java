package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.qna.application.dto.ReadUserInQnaDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadUserInAuctionQuestionResponse(Long id, String name, String image) {

    public static ReadUserInAuctionQuestionResponse of(
            final ReadUserInQnaDto writerDto,
            final String imageRelativeUrl
    ) {
        return new ReadUserInAuctionQuestionResponse(
                writerDto.id(),
                NameProcessor.process(writerDto.isDeleted(), writerDto.name()),
                imageRelativeUrl + writerDto.profileImageStoreName()
        );
    }
}
