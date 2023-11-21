package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.AuctionImage;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JpaAuctionImageRepository extends JpaRepository<AuctionImage, Long> {

    @Query("SELECT i FROM AuctionImage i WHERE i.image.storeName = :storeName")
    Optional<AuctionImage> findByStoreName(final String storeName);
}
