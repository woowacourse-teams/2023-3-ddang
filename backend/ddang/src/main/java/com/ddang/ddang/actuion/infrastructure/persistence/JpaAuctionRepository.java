package com.ddang.ddang.actuion.infrastructure.persistence;

import com.ddang.ddang.actuion.domain.Auction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuctionRepository extends JpaRepository<Auction, Long> {
}
