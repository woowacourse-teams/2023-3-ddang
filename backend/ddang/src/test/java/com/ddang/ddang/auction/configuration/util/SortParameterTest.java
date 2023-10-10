package com.ddang.ddang.auction.configuration.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.auction.configuration.exception.UnsupportedSortParameterException;
import com.ddang.ddang.auction.configuration.util.fixture.SortParameterFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class SortParameterTest extends SortParameterFixture {

    @ParameterizedTest(name = "페이징 정렬 조건으로 {0}가 전달되면 {1}을 반환한다.")
    @CsvSource(value = {
            "new:id", "auctioneer:auctioneerCount", "closingTime:closingTime", "reliability:reliability"
    }, delimiter = ':')
    void 정렬_조건을_파라미터로_반환한다(final String sortParameter, final String expected) {
        // when
        final String actual = SortParameter.findSortProperty(sortParameter);

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 페이징_정렬_조건으로_null을_전달하면_기본_정렬_조건인_id를_반환한다() {
        // when
        final String actual = SortParameter.findSortProperty(null);

        // then
        assertThat(actual).isEqualTo(AuctionSortConditionConsts.ID);
    }

    @Test
    void 지원하지_않는_정렬_조건이면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> SortParameter.findSortProperty(지원하지_않는_정렬))
                .isInstanceOf(UnsupportedSortParameterException.class)
                .hasMessage("지원하지 않는 정렬 방식입니다.");
    }
}
