package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionCondition;
import java.util.Optional;
import org.springframework.data.domain.Slice;

public interface QuerydslAuctionRepository {

    Slice<Auction> findAuctionsAllByLastAuctionId(final ReadAuctionCondition readAuctionCondition);

    Optional<Auction> findAuctionById(final Long auctionId);
}
