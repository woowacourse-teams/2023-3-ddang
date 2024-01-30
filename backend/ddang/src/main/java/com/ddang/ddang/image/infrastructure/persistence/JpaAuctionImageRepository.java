package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.AuctionImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaAuctionImageRepository extends JpaRepository<AuctionImage, Long> {

    @Query("""
        SELECT COUNT(auction_image) > 0
        FROM AuctionImage auction_image
        WHERE auction_image.image.storeName = :storeName
    """)
    boolean existsByStoreName(final String storeName);
}
