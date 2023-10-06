package com.ddang.ddang.region.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.auction.domain.Auction;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionRegionTest {

    @Test
    void 직거래_지역과_경매의_연관관계를_세팅한다() {
        // given
        final Region firstRegion = new Region("서울특별시");
        final Region secondRegion = new Region("강남구");
        final Region thirdRegion = new Region("역삼동");

        secondRegion.addThirdRegion(thirdRegion);
        firstRegion.addSecondRegion(secondRegion);

        final AuctionRegion auctionRegion = new AuctionRegion(firstRegion);

        final Auction auction = Auction.builder()
                                       .title("title")
                                       .build();

        // when
        auctionRegion.initAuction(auction);

        // then
        assertThat(auctionRegion.getAuction()).isNotNull();
    }
}
