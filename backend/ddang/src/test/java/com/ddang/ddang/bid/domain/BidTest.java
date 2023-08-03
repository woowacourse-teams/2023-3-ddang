package com.ddang.ddang.bid.domain;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.auction.domain.BidUnit;
import com.ddang.ddang.auction.domain.Price;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidTest {

    @Test
    void 입찰자가_마지막_입찰자와_동일한_경우_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        final Bid bid = new Bid(auction, user, new BidPrice(10_000));

        // when
        final boolean actual = bid.isSameBidder(user);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰하는_경우_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        final Bid bid = new Bid(auction, user, new BidPrice(10_000));

        // when
        final boolean actual = bid.isBidPriceGreaterThan(new BidPrice(9_000));

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 마지막_입찰액과_최소_입찰단위를_더한_금액보다_낮은_금액으로_입찰하는_경우_참을_반환한다() {
        // given
        final Auction auction = Auction.builder()
                                       .title("경매 상품 1")
                                       .description("이것은 경매 상품 1 입니다.")
                                       .bidUnit(new BidUnit(1_000))
                                       .startPrice(new Price(1_000))
                                       .closingTime(LocalDateTime.now().plusDays(7))
                                       .build();
        final User user = new User("사용자1", "이미지1", 4.9);

        final Bid bid = new Bid(auction, user, new BidPrice(10_000));
        auction.updateLastBid(bid);

        // when
        final boolean actual = bid.isNextBidPriceGreaterThan(new BidPrice(10_900));

        // then
        assertThat(actual).isTrue();
    }
}
