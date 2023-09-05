package com.ddang.ddang.auction.infrastructure.persistence.util;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static org.assertj.core.api.Assertions.assertThat;

import com.querydsl.core.types.OrderSpecifier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.data.domain.Sort.Order;

class AuctionSortConditionConverterTest {

    @ParameterizedTest(name = "condition이 {0}일 때 {1}을 반환한다.")
    @MethodSource("successConvertConditionArguments")
    void 유효한_정렬_조건을_전달하면_Querydsl_표현식을_반환한다(
            final String sortCondition,
            final OrderSpecifier<?> expected
    ) {
        final OrderSpecifier<?> actual = AuctionSortConditionConverter.convert(Order.by(sortCondition));

        assertThat(actual).isEqualTo(expected);
    }

    private static Stream<Arguments> successConvertConditionArguments() {
        return Stream.of(
                Arguments.arguments("id", auction.id.asc()),
                Arguments.arguments("auctioneerCount", auction.auctioneerCount.asc()),
                Arguments.arguments("closingTime", auction.closingTime.asc()),
                Arguments.arguments("reliability", auction.seller.reliability.asc())
        );
    }
}
