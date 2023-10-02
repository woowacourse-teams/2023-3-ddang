package com.ddang.ddang.auction.infrastructure.persistence.fixture;

import java.time.Instant;
import java.time.ZoneId;

@SuppressWarnings("NonAsciiCharacters")
public class JpaAuctionRepositoryFixture {

    protected Instant 시간 = Instant.parse("2023-07-08T22:21:20Z");
    protected ZoneId 위치 = ZoneId.of("UTC");
}
