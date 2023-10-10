package com.ddang.ddang.bid.domain;

import com.ddang.ddang.bid.domain.fixture.BidFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidTest extends BidFixture {

    @Test
    void 입찰자가_마지막_입찰자와_동일한_경우_참을_반환한다() {
        // given
        final Bid bid = new Bid(경매, 입찰자, 입찰액);

        // when
        final boolean actual = bid.isSameBidder(입찰자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰하는_경우_참을_반환한다() {
        // given
        final Bid bid = new Bid(경매, 입찰자, 입찰액);
        경매.updateLastBid(bid);

        // when
        final boolean actual = bid.isNextBidPriceGreaterThan(이전_입찰액보다_작은_입찰액);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 마지막_입찰액과_최소_입찰단위를_더한_금액보다_낮은_금액으로_입찰하는_경우_참을_반환한다() {
        // given
        final Bid bid = new Bid(경매, 입찰자, 입찰액);
        경매.updateLastBid(bid);

        // when
        final boolean actual = bid.isNextBidPriceGreaterThan(이전_입찰액보다_크지만_입찰_단위보다_작은_입찰액);

        // then
        assertThat(actual).isTrue();
    }
}
