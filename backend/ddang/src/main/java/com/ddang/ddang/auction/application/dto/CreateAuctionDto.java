package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;

import java.time.LocalDateTime;

public record CreateAuctionDto(
        String title,
        String description,
        int bidUnit,
        int startBidPrice,
        LocalDateTime closingTime
) {
    public Auction toEntity() {
        return Auction.builder()
                      .title(title)
                      .description(description)
                      .bidUnit(new BidUnit(bidUnit))
                      .startBidPrice(new Price(startBidPrice))
                      .closingTime(closingTime)
                      .build();
    }
}
