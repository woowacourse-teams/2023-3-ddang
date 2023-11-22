package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;

public record CreateInfoAuctionDto(
        Long id,
        String title,
        String auctionImageStoreName,
        int startPrice
) {

    public static CreateInfoAuctionDto from(final Auction auction) {
        return new CreateInfoAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getThumbnailImageStoreName(),
                auction.getStartPrice().getValue()
        );
    }
}
