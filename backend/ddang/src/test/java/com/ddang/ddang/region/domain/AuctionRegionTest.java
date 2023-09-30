package com.ddang.ddang.region.domain;

import com.ddang.ddang.region.domain.fixture.AuctionRegionFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionRegionTest extends AuctionRegionFixture {

    @Test
    void 직거래_지역과_경매의_연관관계를_세팅한다() {
        // given
        final AuctionRegion auctionRegion = new AuctionRegion(서울특별시_하위_강남구_하위_역삼동);

        // when
        auctionRegion.initAuction(경매);

        // then
        assertThat(auctionRegion.getAuction().getTitle()).isEqualTo(경매.getTitle());
    }
}
