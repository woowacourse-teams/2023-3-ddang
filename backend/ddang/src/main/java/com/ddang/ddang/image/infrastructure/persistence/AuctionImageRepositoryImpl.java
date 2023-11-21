package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AuctionImageRepositoryImpl implements AuctionImageRepository {

    private final JpaAuctionImageRepository jpaAuctionImageRepository;

    @Override
    public Optional<AuctionImage> findByStoreName(final String storeName) {
        return jpaAuctionImageRepository.findByStoreName(storeName);
    }
}
