package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;

import java.util.List;
import java.util.Optional;

public interface QuerydslAuctionRepository {

    List<Auction> findAuctionsAllByLastAuctionId(final Long lastAuctionId, final int size);

    Optional<Auction> findAuctionWithRegionsById(final Long auctionId);
}
