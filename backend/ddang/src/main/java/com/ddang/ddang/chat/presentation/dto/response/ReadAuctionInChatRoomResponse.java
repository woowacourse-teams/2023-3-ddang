package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.image.util.ImageBaseUrl;
import com.ddang.ddang.image.util.ImageUrlBuilder;

public record ReadAuctionInChatRoomResponse(Long id, String title, String image, int price) {

    public static ReadAuctionInChatRoomResponse from(final ReadAuctionInChatRoomDto dto) {
        final Long thumbNailImageId = dto.auctionImageIds().get(0);
        final String imageUrl = ImageUrlBuilder.calculate(ImageBaseUrl.AUCTION, thumbNailImageId);

        return new ReadAuctionInChatRoomResponse(dto.id(), dto.title(), imageUrl, dto.lastBidPrice());
    }
}
