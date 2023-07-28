package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.user.domain.User;

public record CreateBidDto(Long auctionId, int price) {
    public Bid toEntity(final Auction auction, final User user) {
        return new Bid(auction, user, new Price(price));
    }
}
