package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;

public record ReadAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static ReadAuctionResponse of(final ReadAuctionDto dto, final String imageRelativeUrl) {
        return new ReadAuctionResponse(
                dto.id(),
                dto.title(),
                calculateThumbnailImageUrl(dto, imageRelativeUrl),
                processAuctionPrice(dto.startPrice(), dto.lastBidPrice()),
                dto.auctionStatus().name(),
                dto.auctioneerCount()
        );
    }

    private static String calculateThumbnailImageUrl(final ReadAuctionDto dto, final String imageRelativeUrl) {
        final String thumbnailAuctionImageStoreName = dto.auctionImageStoreNames().get(0);

        return imageRelativeUrl + thumbnailAuctionImageStoreName;
    }

    private static int processAuctionPrice(final Integer startPrice, final Integer lastBidPrice) {
        if (lastBidPrice == null) {
            return startPrice;
        }

        return lastBidPrice;
    }
}
