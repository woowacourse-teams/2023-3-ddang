package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.repository.AuctionRepository;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
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
    public Auction getTotalAuctionByIdOrThrow(final Long id) {
        return jpaAuctionRepository.findTotalAuctionById(id)
                                   .orElseThrow(() -> new AuctionNotFoundException("지정한 경매를 찾을 수 없습니다."));
    }

    @Override
    public Auction getPureAuctionByIdOrThrow(final Long id) {
        return jpaAuctionRepository.findPureAuctionById(id)
                                   .orElseThrow(() -> new AuctionNotFoundException("지정한 경매를 찾을 수 없습니다."));
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
    public boolean existsBySellerIdAndAuctionStatusIsOngoing(final Long userId, final LocalDateTime now) {
        return jpaAuctionRepository.existsBySellerIdAndDeletedIsFalseAndClosingTimeGreaterThanEqual(userId, now);
    }

    @Override
    public boolean existsLastBidByUserIdAndAuctionStatusIsOngoing(final Long userId, final LocalDateTime now) {
        return jpaAuctionRepository.existsByLastBidBidderIdAndDeletedIsFalseAndClosingTimeGreaterThanEqual(
                userId,
                now
        );
    }
}
