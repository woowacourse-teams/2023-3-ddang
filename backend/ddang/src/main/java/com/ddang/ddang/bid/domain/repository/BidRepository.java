package com.ddang.ddang.bid.domain.repository;

import com.ddang.ddang.bid.domain.Bid;

import java.util.List;

public interface BidRepository {

    Bid save(final Bid bid);

    List<Bid> findAllByAuctionId(final Long auctionId);
}
