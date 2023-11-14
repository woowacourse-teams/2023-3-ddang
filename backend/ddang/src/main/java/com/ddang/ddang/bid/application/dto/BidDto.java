package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.auction.domain.Auction;

public record BidDto(Long previousBidderId, Auction auction, String auctionImageAbsoluteUrl) {
}
