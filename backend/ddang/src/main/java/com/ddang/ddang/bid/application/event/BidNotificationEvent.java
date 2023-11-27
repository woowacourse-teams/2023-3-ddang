package com.ddang.ddang.bid.application.event;

import com.ddang.ddang.auction.domain.Auction;

public record BidNotificationEvent(Long previousBidderId, Auction auction, String auctionImageAbsoluteUrl) {
}
