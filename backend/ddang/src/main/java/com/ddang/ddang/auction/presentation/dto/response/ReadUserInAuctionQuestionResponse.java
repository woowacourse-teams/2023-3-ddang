package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.qna.application.dto.ReadUserInQnaDto;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record ReadUserInAuctionQuestionResponse(Long id, String name, String image) {

    public static ReadUserInAuctionQuestionResponse from(final ReadUserInQnaDto writerDto) {
        return new ReadUserInAuctionQuestionResponse(
                writerDto.id(),
                NameProcessor.process(writerDto.isDeleted(), writerDto.name()),
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, writerDto.profileImageId())
        );
    }
}
