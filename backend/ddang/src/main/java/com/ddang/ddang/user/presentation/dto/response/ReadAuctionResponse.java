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

    public static ReadAuctionResponse of(final ReadAuctionDto dto, final String baseUrl) {
        return new ReadAuctionResponse(
                dto.id(),
                dto.title(),
                convertImageUrl(dto, baseUrl),
                processAuctionPrice(dto.startPrice(), dto.lastBidPrice()),
                dto.auctionStatus().name(),
                dto.auctioneerCount()
        );
    }

    private static String convertImageUrl(final ReadAuctionDto dto, final String baseUrl) {
        return baseUrl.concat(String.valueOf(dto.auctionImageIds().get(0)));
    }

    private static int processAuctionPrice(final Integer startPrice, final Integer lastBidPrice) {
        if (lastBidPrice == null) {
            return startPrice;
        }

        return lastBidPrice;
    }
}
