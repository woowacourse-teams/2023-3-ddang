package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;

import java.util.List;

public interface QuerydslAuctionRepository {

    List<Auction> findAuctionsAllByLastAuctionId(final Long lastAuctionId, final int size);
}
