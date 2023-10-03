package com.ddang.ddang.auction.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.ddang.ddang.auction.domain.exception.InvalidPriceValueException;
import com.ddang.ddang.auction.domain.fixture.BidUnitFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidUnitTest extends BidUnitFixture {

    @Test
    void 가능한_범위_내의_가격을_받는_경우_정상적으로_생성된다() {
        // when & then
        assertThatCode(() -> new BidUnit(1_000)).doesNotThrowAnyException();
    }

    @Test
    void 최소_가격_미만으로_받는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new BidUnit(최소_금액보다_낮은_입찰_단위))
                .isInstanceOf(InvalidPriceValueException.class)
                .hasMessage("입찰 단위는 0원 이상이어야 합니다.");
    }

    @Test
    void 최대_가격_초과하여_받는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new BidUnit(최대_금액보다_높은_입찰_단위))
                .isInstanceOf(InvalidPriceValueException.class)
                .hasMessage("입찰 단위는 2100000000원 이하여야 합니다.");
    }

    @Test
    void 유효하지_않은_가격_단위를_받는_경우_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new BidUnit(단위가_100이_아닌_입찰_단위))
                .isInstanceOf(InvalidPriceValueException.class)
                .hasMessage("입찰 단위는 100의 배수여야 합니다.");
    }
}
