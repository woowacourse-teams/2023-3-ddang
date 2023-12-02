package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.domain.AuctionStatus;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

public record CreateAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static CreateAuctionResponse from(final CreateInfoAuctionDto dto) {
        return new CreateAuctionResponse(
                dto.id(),
                dto.title(),
                convertAuctionImageUrl(dto.auctionStoreName()),
                dto.startPrice(),
                AuctionStatus.UNBIDDEN.name(),
                0
        );
    }

    private static String convertAuctionImageUrl(final String storeName) {
        return ImageUrlCalculator.calculateBy(ImageRelativeUrl.AUCTION, storeName);
    }
}
