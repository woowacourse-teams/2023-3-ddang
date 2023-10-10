package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.qna.application.dto.ReadUserInQnaDto;

public record ReadUserInAuctionQuestionResponse(Long id, String name, String image) {

    public static ReadUserInAuctionQuestionResponse from(
            final ReadUserInQnaDto writerDto
    ) {
        return new ReadUserInAuctionQuestionResponse(
                writerDto.id(),
                writerDto.name(),
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, writerDto.profileImageId())
        );
    }
}
