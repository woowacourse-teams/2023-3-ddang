package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.auction.domain.dto.AuctionAndImageDto;

public record BidDto(
        Long previousBidderId,
        AuctionAndImageDto auctionAndImageDto,
        String auctionImageAbsoluteUrl
) {
}
