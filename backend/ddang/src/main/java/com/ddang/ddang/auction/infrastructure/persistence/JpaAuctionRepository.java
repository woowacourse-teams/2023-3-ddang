package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import org.springframework.data.jpa.repository.Query;

public interface JpaAuctionRepository extends JpaRepository<Auction, Long>, QuerydslAuctionRepository, QuerydslAuctionAndImageRepository {

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
        JOIN FETCH a.seller
        WHERE a.deleted = false AND a.id = :id
    """)
    Optional<Auction> findAuctionById(final Long id);
}
