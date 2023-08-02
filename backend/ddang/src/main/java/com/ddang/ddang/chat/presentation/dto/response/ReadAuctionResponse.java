package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;

public record ReadAuctionResponse(Long id, String title, String image) {

    public static ReadAuctionResponse of(ReadAuctionDto dto, String baseUrl) {
        final Long thumbNailImageId = dto.auctionImageIds().get(0);
        final String imageUrl = baseUrl.concat(String.valueOf(thumbNailImageId));
        return new ReadAuctionResponse(dto.id(), dto.title(), imageUrl);
    }
}
