package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {

    @Query("""
        SELECT a
        FROM Auction a
        LEFT JOIN FETCH a.auctionRegions ar
        LEFT JOIN FETCh ar.thirdRegion tr
        LEFT JOIN FETCH tr.firstRegion
        LEFT JOIN FETCH tr.secondRegion
        LEFT JOIN FETCH a.lastBid
        JOIN FETCH a.subCategory sc
        JOIN FETCH sc.mainCategory
        JOIN FETCH a.seller seller
        LEFT JOIN FETCH seller.profileImage
        WHERE a.deleted = false AND a.id = :id
    """)
    Optional<Auction> findTotalAuctionById(final Long id);

    @Query("SELECT a FROM Auction a WHERE a.deleted = false AND a.id = :id")
    Optional<Auction> findPureAuctionById(final Long id);

    boolean existsBySellerIdAndDeletedIsFalseAndClosingTimeGreaterThanEqual(
            final Long userId,
            final LocalDateTime now
    );

    boolean existsByLastBidBidderIdAndDeletedIsFalseAndClosingTimeGreaterThanEqual(
            final Long userId,
            final LocalDateTime now
    );
}
