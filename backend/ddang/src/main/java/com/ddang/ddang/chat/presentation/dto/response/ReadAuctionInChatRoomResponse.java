package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

public record ReadAuctionInChatRoomResponse(Long id, String title, String image, int price) {

    public static ReadAuctionInChatRoomResponse from(final ReadAuctionInChatRoomDto dto) {
        final String thumbNailImageUrl = ImageUrlCalculator.calculateBy(ImageRelativeUrl.AUCTION, dto.thumbnailImageId());

        return new ReadAuctionInChatRoomResponse(dto.id(), dto.title(), thumbNailImageUrl, dto.lastBidPrice());
    }
}
