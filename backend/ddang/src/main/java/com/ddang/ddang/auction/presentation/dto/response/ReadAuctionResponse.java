package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;

// TODO 2차 데모데이 이후 리팩토링 예정
public record ReadAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static ReadAuctionResponse from(final ReadAuctionDto dto) {
        return new ReadAuctionResponse(
                dto.id(),
                dto.title(),
                dto.image(),
                dto.startBidPrice(),
                "unbidden",
                0
        );
    }
}
