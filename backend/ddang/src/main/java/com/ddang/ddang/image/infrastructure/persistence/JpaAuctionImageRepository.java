package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.AuctionImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAuctionImageRepository extends JpaRepository<AuctionImage, Long> {

}
