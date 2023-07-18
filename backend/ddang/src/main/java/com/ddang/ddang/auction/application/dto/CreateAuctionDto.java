package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.auction.presentation.dto.CreateAuctionRequest;

import java.time.LocalDateTime;

public record CreateAuctionDto(
        String title,
        String description,
        int bidUnit,
        int startBidPrice,
        LocalDateTime closingTime
) {

    public static CreateAuctionDto from(final CreateAuctionRequest request) {
        return new CreateAuctionDto(
                request.title(),
                request.description(),
                request.bidUnit(),
                request.startBidPrice(),
                request.closingTime()
        );
    }

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
