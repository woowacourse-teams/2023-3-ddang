package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class AuctionImageRepositoryImpl implements AuctionImageRepository {

    private final JpaAuctionImageRepository jpaAuctionImageRepository;

    @Override
    public Optional<AuctionImage> findById(final Long id) {
        return jpaAuctionImageRepository.findById(id);
    }
}
