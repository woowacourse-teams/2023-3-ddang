package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

public record ReadAuctionInChatRoomResponse(Long id, String title, String image, int price) {

    public static ReadAuctionInChatRoomResponse of(
            final ReadAuctionInChatRoomDto dto,
            final ImageRelativeUrl imageRelativeUrl
    ) {
        return new ReadAuctionInChatRoomResponse(
                dto.id(),
                dto.title(),
                imageRelativeUrl.calculateAbsoluteUrl() + dto.thumbnailImageStoreName(),
                dto.lastBidPrice()
        );
    }
}
