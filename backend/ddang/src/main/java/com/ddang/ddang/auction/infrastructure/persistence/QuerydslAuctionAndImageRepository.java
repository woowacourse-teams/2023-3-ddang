package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.infrastructure.persistence.dto.AuctionAndImageDto;

import java.util.Optional;

public interface QuerydslAuctionAndImageRepository {

    Optional<AuctionAndImageDto> findDtoByAuctionId(final Long auctionId);
}
