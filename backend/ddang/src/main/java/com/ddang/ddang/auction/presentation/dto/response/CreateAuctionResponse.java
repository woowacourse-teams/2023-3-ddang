package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;

public record CreateAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static CreateAuctionResponse of(final CreateInfoAuctionDto dto, final String baseUrl) {
        return new CreateAuctionResponse(
                dto.id(),
                dto.title(),
                baseUrl.concat(String.valueOf(dto.auctionImageId())),
                dto.startPrice(),
                // TODO 2차 데모데이 이후 enum으로 처리
                "UNBIDDEN",
                0
        );
    }
}
