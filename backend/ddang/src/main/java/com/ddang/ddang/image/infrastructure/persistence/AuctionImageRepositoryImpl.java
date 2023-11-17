package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuctionImageRepositoryImpl implements AuctionImageRepository {

    private final JpaAuctionImageRepository jpaAuctionImageRepository;

    @Override
    public boolean existsByStoreName(final String storeName) {
        return jpaAuctionImageRepository.existsByStoreName(storeName);
    }
}
