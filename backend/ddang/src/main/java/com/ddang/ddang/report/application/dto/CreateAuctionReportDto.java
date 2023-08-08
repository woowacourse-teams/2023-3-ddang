package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.user.domain.User;

public record CreateAuctionReportDto(Long auctionId, String description) {

    public AuctionReport toEntity(final User reporter, final Auction auction) {
        return new AuctionReport(reporter, auction, this.description);
    }
}
