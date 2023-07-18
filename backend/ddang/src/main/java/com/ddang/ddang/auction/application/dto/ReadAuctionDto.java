package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;

import java.time.LocalDateTime;

public record ReadAuctionDto(
        Long id,
        String title,
        String description,
        int bidUnit,
        int startBidPrice,
        Integer lastBidPrice,
        Integer winningBidPrice,
        boolean deleted,
        LocalDateTime registerTime,
        LocalDateTime closingTime
) {

    public static ReadAuctionDto from(final Auction auction) {
        return new ReadAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getBidUnit().getValue(),
                auction.getStartBidPrice().getValue(),
                convertPrice(auction.getLastBidPrice()),
                convertPrice(auction.getWinningBidPrice()),
                auction.isDeleted(),
                auction.getCreatedTime(),
                auction.getClosingTime()
        );
    }

    private static Integer convertPrice(final Price price) {
        if (price == null) {
            return null;
        }

        return price.getValue();
    }
}
