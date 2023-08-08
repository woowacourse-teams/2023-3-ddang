package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.auction.domain.Auction;

import java.time.LocalDateTime;

public record ReadAuctionDto(
        Long id,
        String title,
        String description,
        int bidUnit,
        int startPrice,
        boolean deleted,
        LocalDateTime closingTime,
        int auctioneerCount
) {

    public static ReadAuctionDto from(final Auction auction) {
        return new ReadAuctionDto(
                auction.getId(),
                auction.getTitle(),
                auction.getDescription(),
                auction.getBidUnit().getValue(),
                auction.getStartPrice().getValue(),
                auction.isDeleted(),
                auction.getClosingTime(),
                auction.getAuctioneerCount()
        );
    }
}
