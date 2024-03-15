package com.ddang.ddang.auction.domain.repository;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.ddang.ddang.bid.domain.Bid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface AuctionRepository {

    Auction save(final Auction auction);

    boolean existsById(final Long id);

    Optional<Auction> findTotalAuctionById(final Long id);

    Optional<Auction> findPureAuctionById(final Long id);

    Slice<Auction> findAuctionsAllByCondition(
            final ReadAuctionSearchCondition readAuctionSearchCondition,
            final Pageable pageable
    );

    Slice<Auction> findAuctionsAllByUserId(final Long userId, final Pageable pageable);

    Slice<Auction> findAuctionsAllByBidderId(final Long bidderId, final Pageable pageable);

    List<Auction> findAll();

    boolean existsBySellerIdAndAuctionStatusIsOngoing(final Long userId, final LocalDateTime now);

    boolean existsLastBidByUserIdAndAuctionStatusIsOngoing(final Long userId, final LocalDateTime now);
}
