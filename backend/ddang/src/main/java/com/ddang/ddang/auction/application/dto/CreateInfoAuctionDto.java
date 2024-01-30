package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.image.application.util.ImageStoreNameProcessor;

public record CreateInfoAuctionDto(
        Long id,
        String title,
        String auctionStoreName,
        int startPrice
) {

    public static CreateInfoAuctionDto from(final Auction auction) {
        return new CreateInfoAuctionDto(
                auction.getId(),
                auction.getTitle(),
                ImageStoreNameProcessor.process(auction.getAuctionImages().get(0)),
                auction.getStartPrice().getValue()
        );
    }
}
