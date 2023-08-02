package com.ddang.ddang.bid.domain;

import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidPriceTest {

    @Test
    void 가능한_범위_내의_입찰_가격을_받는_경우_정상적으로_생성된다() {
        // when & then
        assertThatCode(() -> new BidPrice(1_000)).doesNotThrowAnyException();
    }

    @Test
    void 최소_입찰_가격_미만으로_받는_경우_예외가_발생한다() {
        // given
        final int invalidPrice = -1;

        // when & then
        assertThatThrownBy(() -> new BidPrice(invalidPrice)).isInstanceOf(InvalidBidPriceException.class)
                                                            .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 최대_입찰_가격_초과하여_받는_경우_예외가_발생한다() {
        // given
        final int invalidPrice = Integer.MAX_VALUE;

        // when & then
        assertThatThrownBy(() -> new BidPrice(invalidPrice)).isInstanceOf(InvalidBidPriceException.class)
                                                            .hasMessage("가격은 2100000000원 이하여야 합니다.");
    }

    @Test
    void 다른_입찰액과_비교했을_때_크다면_참을_반환한다() {
        // given
        final BidPrice bidPrice = new BidPrice(10_000);
        final BidPrice otherBidPrice = new BidPrice(9_000);

        // when
        final boolean actual = bidPrice.isGreaterThan(otherBidPrice);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 다른_입찰액과_비교했을_때_작다면_같다면_거짓을_반환한다() {
        // given
        final BidPrice bidPrice = new BidPrice(9_000);
        final BidPrice otherBidPrice = new BidPrice(10_000);

        // when
        final boolean actual = bidPrice.isGreaterThan(otherBidPrice);

        // then
        assertThat(actual).isFalse();
    }
}