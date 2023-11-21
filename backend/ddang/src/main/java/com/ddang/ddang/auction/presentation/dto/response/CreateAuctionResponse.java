package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

public record CreateAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static CreateAuctionResponse of(final CreateInfoAuctionDto dto, final ImageRelativeUrl imageRelativeUrl) {
        return new CreateAuctionResponse(
                dto.id(),
                dto.title(),
                imageRelativeUrl.calculateAbsoluteUrl() + dto.auctionImageStoreName(),
                dto.startPrice(),
                AuctionStatus.UNBIDDEN.name(),
                0
        );
    }
}
