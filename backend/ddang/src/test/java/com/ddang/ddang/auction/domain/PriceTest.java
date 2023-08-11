package com.ddang.ddang.auction.domain;

import com.ddang.ddang.auction.domain.exception.InvalidPriceValueException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class PriceTest {

    @Test
    void 가능한_범위_내의_가격을_받는_경우_정상적으로_생성된다() {
        // when & then
        assertThatCode(() -> new Price(1_000)).doesNotThrowAnyException();
    }

    @Test
    void 최소_가격_미만으로_받는_경우_예외가_발생한다() {
        // given
        final int invalidPrice = -1;

        // when & then
        assertThatThrownBy(() -> new Price(invalidPrice))
                .isInstanceOf(InvalidPriceValueException.class)
                .hasMessage("가격은 0원 이상이어야 합니다.");
    }

    @Test
    void 최대_가격_초과하여_받는_경우_예외가_발생한다() {
        // given
        final int invalidPrice = Integer.MAX_VALUE;

        // when & then
        assertThatThrownBy(() -> new Price(invalidPrice))
                .isInstanceOf(InvalidPriceValueException.class)
                .hasMessage("가격은 2100000000원 이하여야 합니다.");
    }
}
