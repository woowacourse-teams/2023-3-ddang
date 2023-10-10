package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

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
                calculateThumbnailImageUrl(dto),
                processAuctionPrice(dto.startPrice(), dto.lastBidPrice()),
                dto.auctionStatus().name(),
                dto.auctioneerCount()
        );
    }

    private static String calculateThumbnailImageUrl(final ReadAuctionDto dto) {
        final Long thumbnailAuctionImage = dto.auctionImageIds().get(0);

        return ImageUrlCalculator.calculateBy(ImageRelativeUrl.AUCTION, thumbnailAuctionImage);
    }

    private static int processAuctionPrice(final Integer startPrice, final Integer lastBidPrice) {
        if (lastBidPrice == null) {
            return startPrice;
        }

        return lastBidPrice;
    }
}
