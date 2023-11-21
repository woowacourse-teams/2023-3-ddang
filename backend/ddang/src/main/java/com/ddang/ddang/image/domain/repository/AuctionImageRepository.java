package com.ddang.ddang.image.domain.repository;

import com.ddang.ddang.image.domain.AuctionImage;

public interface AuctionImageRepository {

    AuctionImage getByStoreNameOrThrow(final String storeName);
}
