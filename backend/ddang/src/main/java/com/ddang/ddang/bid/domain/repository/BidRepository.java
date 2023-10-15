package com.ddang.ddang.bid.domain.repository;

import com.ddang.ddang.bid.domain.Bid;

import java.util.List;
import java.util.Optional;

public interface BidRepository {

    Bid save(final Bid bid);

    List<Bid> findAllByAuctionId(final Long auctionId);

    Optional<Bid> findLastBidByAuctionId(final Long auctionId);
}
