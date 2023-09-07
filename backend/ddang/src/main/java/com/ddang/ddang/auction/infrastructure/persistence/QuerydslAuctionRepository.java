package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.SearchCondition;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QuerydslAuctionRepository {

    Slice<Auction> findAuctionsAllByLastAuctionId(
            final Long lastAuctionId,
            final Pageable pageable,
            final SearchCondition searchCondition
    );

    Optional<Auction> findAuctionById(final Long auctionId);
}
