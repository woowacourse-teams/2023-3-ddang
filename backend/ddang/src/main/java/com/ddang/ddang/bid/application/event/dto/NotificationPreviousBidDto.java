package com.ddang.ddang.bid.application.event.dto;

import com.ddang.ddang.auction.domain.Auction;

public record NotificationPreviousBidDto(Long previousBidderId, Auction auction, String auctionImageAbsoluteUrl) {
}
