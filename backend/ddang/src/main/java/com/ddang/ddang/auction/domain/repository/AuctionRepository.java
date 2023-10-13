package com.ddang.ddang.auction.domain.repository;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AuctionRepository {

    Auction save(final Auction auction);

    Optional<Auction> findTotalAuctionById(final Long id);

    Optional<Auction> findPureAuctionById(final Long id);

    Slice<Auction> findAuctionsAllByCondition(
            final ReadAuctionSearchCondition readAuctionSearchCondition,
            final Pageable pageable
    );

    Slice<Auction> findAuctionsAllByUserId(final Long userId, final Pageable pageable);

    Slice<Auction> findAuctionsAllByBidderId(final Long bidderId, final Pageable pageable);
}
