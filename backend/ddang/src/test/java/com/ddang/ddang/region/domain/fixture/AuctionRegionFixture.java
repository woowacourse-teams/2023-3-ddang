package com.ddang.ddang.region.domain.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.region.domain.Region;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionRegionFixture {

    protected Region 서울특별시_하위_강남구_하위_역삼동;
    protected Auction 경매;

    @BeforeEach
    void setUp() {
        Region 서울특별시 = new Region("서울특별시");
        Region 서울특별시_하위_강남구 = new Region("강남구");
        서울특별시_하위_강남구_하위_역삼동 = new Region("역삼동");

        서울특별시_하위_강남구.addThirdRegion(서울특별시_하위_강남구_하위_역삼동);
        서울특별시.addSecondRegion(서울특별시_하위_강남구);

        경매 = Auction.builder()
                     .title("title")
                     .build();
    }
}
