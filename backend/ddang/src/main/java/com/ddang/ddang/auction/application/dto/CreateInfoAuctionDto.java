package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;

public record CreateInfoAuctionDto(
        Long id,
        String title,
        Long auctionImageId,
        int startPrice
) {

    public static CreateInfoAuctionDto from(final Auction auction) {
        return new CreateInfoAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getAuctionImages().get(0).getId(),
                auction.getStartPrice().getValue()
        );
    }
}
