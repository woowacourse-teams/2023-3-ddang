package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QuerydslAuctionRepository {

    Slice<Auction> findAuctionsAllByCondition(
            final Pageable pageable,
            final ReadAuctionSearchCondition readAuctionSearchCondition
    );

    Slice<Auction> findAuctionsAllByUserId(final Long userId, final Pageable pageable);

    Slice<Auction> findAuctionsAllByBidderId(final Long bidderId, final Pageable pageable);
}
