package com.ddang.ddang.image.domain.repository;

import com.ddang.ddang.image.domain.AuctionImage;

import java.util.Optional;

public interface AuctionImageRepository {

    Optional<AuctionImage> findById(final Long id);
}
