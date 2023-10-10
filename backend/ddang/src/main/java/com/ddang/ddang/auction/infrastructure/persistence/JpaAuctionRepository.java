package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAuctionRepository extends JpaRepository<Auction, Long>, QuerydslAuctionRepository, QuerydslAuctionAndImageRepository {

    Optional<Auction> findByIdAndDeletedIsFalse(final Long id);
}
