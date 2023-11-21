package com.ddang.ddang.image.infrastructure.persistence;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.repository.AuctionImageRepository;
import com.ddang.ddang.image.infrastructure.persistence.exception.AuctionImageNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionImageRepositoryImpl implements AuctionImageRepository {

    private final JpaAuctionImageRepository jpaAuctionImageRepository;

    public AuctionImage getByStoreNameOrThrow(final String storeName) {
        return jpaAuctionImageRepository.findByStoreName(storeName)
                                        .orElseThrow(() ->
                                                new AuctionImageNotFoundException("경매 이미지를 찾을 수 없습니다.")
                                        );
    }
}
