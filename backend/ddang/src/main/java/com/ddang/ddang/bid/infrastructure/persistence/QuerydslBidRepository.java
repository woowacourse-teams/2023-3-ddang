package com.ddang.ddang.bid.infrastructure.persistence;

import com.ddang.ddang.bid.domain.Bid;

public interface QuerydslBidRepository {

    Bid findLastBidByAuctionId(final Long id);
}
