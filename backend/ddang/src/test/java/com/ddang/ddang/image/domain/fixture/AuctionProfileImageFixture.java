package com.ddang.ddang.image.domain.fixture;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;

import java.time.LocalDateTime;

@SuppressWarnings("NonAsciiCharacters")
public class AuctionProfileImageFixture {

    protected Auction 경매 = Auction.builder()
                                  .title("경매 상품")
                                  .description("이것은 경매 상품입니다.")
                                  .bidUnit(new BidUnit(1_000))
                                  .startPrice(new Price(1_000))
                                  .closingTime(LocalDateTime.now())
                                  .build();
}
