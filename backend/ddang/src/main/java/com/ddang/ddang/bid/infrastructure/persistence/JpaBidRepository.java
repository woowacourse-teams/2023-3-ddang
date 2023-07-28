package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.bid.domain.Bid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaBidRepository extends JpaRepository<Bid, Long>, QuerydslBidRepository {

    List<Bid> findByAuctionId(final Long id);
}
