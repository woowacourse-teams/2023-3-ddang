package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import java.util.Optional;
import org.springframework.data.domain.Slice;

public interface QuerydslAuctionRepository {

    Slice<Auction> findAuctionsAllByLastAuctionId(final Long lastAuctionId, final int size);

    Optional<Auction> findAuctionById(final Long auctionId);
}
