package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;

import java.time.LocalDateTime;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionRepositoryImpl implements AuctionRepository {

    private final JpaAuctionRepository jpaAuctionRepository;
    private final QuerydslAuctionRepository querydslAuctionRepository;

    @Override
    public Auction save(final Auction auction) {
        return jpaAuctionRepository.save(auction);
    }

    @Override
    public boolean existsById(final Long id) {
        return jpaAuctionRepository.existsById(id);
    }

    @Override
    public Optional<Auction> findTotalAuctionById(final Long id) {
        return jpaAuctionRepository.findTotalAuctionById(id);
    }

    @Override
    public Optional<Auction> findPureAuctionById(final Long id) {
        return jpaAuctionRepository.findPureAuctionById(id);
    }

    @Override
    public Slice<Auction> findAuctionsAllByCondition(
            final ReadAuctionSearchCondition readAuctionSearchCondition,
            final Pageable pageable
    ) {
        return querydslAuctionRepository.findAuctionsAllByCondition(readAuctionSearchCondition, pageable);
    }

    @Override
    public Slice<Auction> findAuctionsAllByUserId(final Long userId, final Pageable pageable) {
        return querydslAuctionRepository.findAuctionsAllByUserId(userId, pageable);
    }

    @Override
    public Slice<Auction> findAuctionsAllByBidderId(final Long bidderId, final Pageable pageable) {
        return querydslAuctionRepository.findAuctionsAllByBidderId(bidderId, pageable);
    }

    @Override
    public boolean existsBySellerIdAndAuctionIsOngoing(final Long userId, final LocalDateTime now) {
        return jpaAuctionRepository.existsBySellerIdAndClosingTimeGreaterThanEqual(userId, now);
    }

    @Override
    public boolean existsLastBidByUserIdAndAuctionIsOngoing(final Long userId, final LocalDateTime now) {
        return jpaAuctionRepository.existsByLastBidderIdAndClosingTimeGreaterThanEqual(userId, now);
    }
}
