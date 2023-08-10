package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;

public record ReadAuctionInChatRoomResponse(Long id, String title, String image, int price) {

    public static ReadAuctionInChatRoomResponse of(final ReadAuctionInChatRoomDto dto, final String baseUrl) {
        final Long thumbNailImageId = dto.auctionImageIds().get(0);
        final String imageUrl = baseUrl.concat(String.valueOf(thumbNailImageId));

        return new ReadAuctionInChatRoomResponse(dto.id(), dto.title(), imageUrl, dto.lastBidPrice());
    }
}
