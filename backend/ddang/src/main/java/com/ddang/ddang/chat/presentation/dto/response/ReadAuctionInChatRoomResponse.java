package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadAuctionInChatRoomDto;
import com.ddang.ddang.image.presentation.util.ImageBaseUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

public record ReadAuctionInChatRoomResponse(Long id, String title, String image, int price) {

    public static ReadAuctionInChatRoomResponse of(final ReadAuctionInChatRoomDto dto, final String baseUrl) {
        final Long thumbNailImageId = ImageUrlCalculator.calculate(ImageBaseUrl.AUCTION, dto.thumbnailImageId());
        final String imageUrl = baseUrl.concat(String.valueOf(thumbNailImageId));

        return new ReadAuctionInChatRoomResponse(dto.id(), dto.title(), imageUrl, dto.lastBidPrice());
    }
}
