package com.ddang.ddang.auction.infrastructure.persistence;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface QuerydslAuctionRepository {

    Slice<Auction> findAuctionsAllByLastAuctionId(
            final Pageable pageable,
            final ReadAuctionSearchCondition readAuctionSearchCondition
    );

    Optional<Auction> findAuctionById(final Long auctionId);
}
