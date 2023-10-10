package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.auction.infrastructure.persistence.dto.AuctionAndImageDto;

public record BidDto(
        Long previousBidderId,
        AuctionAndImageDto auctionAndImageDto,
        String auctionImageAbsoluteUrl
) {
}
