package com.ddang.ddang.bid.application.dto;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.bid.domain.Bid;
import com.ddang.ddang.bid.domain.BidPrice;
import com.ddang.ddang.bid.presentation.dto.request.CreateBidRequest;
import com.ddang.ddang.user.domain.User;

public record CreateBidDto(Long auctionId, int bidPrice, Long userId) {

    public static CreateBidDto of(final CreateBidRequest bidRequest, final Long userId) {
        return new CreateBidDto(bidRequest.auctionId(), bidRequest.bidPrice(), userId);
    }

    public Bid toEntity(final Auction auction, final User user) {
        return new Bid(auction, user, new BidPrice(bidPrice));
    }
}
