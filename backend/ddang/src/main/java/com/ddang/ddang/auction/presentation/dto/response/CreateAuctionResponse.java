package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.domain.AuctionStatus;

public record CreateAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static CreateAuctionResponse of(final CreateInfoAuctionDto dto, final String imageRelativeUrl) {
        return new CreateAuctionResponse(
                dto.id(),
                dto.title(),
                imageRelativeUrl + dto.auctionImageStoreName(),
                dto.startPrice(),
                AuctionStatus.UNBIDDEN.name(),
                0
        );
    }
}
