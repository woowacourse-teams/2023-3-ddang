package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;

public record ReadAuctionInChatRoomResponse(Long id, String title, String image, int price) {

    public static ReadAuctionInChatRoomResponse of(
            final ReadAuctionInChatRoomDto dto,
            final String imageRelativeUrl
    ) {
        return new ReadAuctionInChatRoomResponse(
                dto.id(),
                dto.title(),
                imageRelativeUrl + dto.thumbnailImageStoreName(),
                dto.lastBidPrice()
        );
    }
}
