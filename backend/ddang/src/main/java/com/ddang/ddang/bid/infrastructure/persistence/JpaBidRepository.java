package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.bid.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaBidRepository extends JpaRepository<Bid, Long> {

    List<Bid> findAllByAuctionIdOrderByIdAsc(final Long auctionId);

    @Query("select b from Bid b where b.auction.id = :auctionId order by b.id desc limit 1")
    Bid findLastBidByAuctionId(@Param("auctionId") final Long auctionId);
}
