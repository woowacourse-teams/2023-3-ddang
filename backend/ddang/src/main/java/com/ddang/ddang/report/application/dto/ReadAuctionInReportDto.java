package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.auction.domain.Auction;

import java.time.LocalDateTime;

public record ReadAuctionInReportDto(
        Long id,
        ReadUserInReportDto seller,
        String title,
        String description,
        int bidUnit,
        int startPrice,
        boolean deleted,
        LocalDateTime closingTime,
        int auctioneerCount
) {

    public static ReadAuctionInReportDto from(final Auction auction) {
        return new ReadAuctionInReportDto(
                auction.getId(),
                ReadUserInReportDto.from(auction.getSeller()),

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
