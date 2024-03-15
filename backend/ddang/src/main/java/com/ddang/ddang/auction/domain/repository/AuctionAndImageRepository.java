package com.ddang.ddang.auction.domain.repository;

import com.ddang.ddang.auction.domain.dto.AuctionAndImageDto;
import java.util.Optional;

public interface AuctionAndImageRepository {

    Optional<AuctionAndImageDto> findDtoByAuctionId(final Long auctionId);

    Optional<AuctionAndImageDto> findDtoByAuctionIdWithLock(final Long auctionId);
}
