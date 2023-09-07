package com.ddang.ddang.auction.infrastructure.persistence.util;

import static com.ddang.ddang.auction.domain.QAuction.auction;
import static org.springframework.data.domain.Sort.Order;

import com.ddang.ddang.auction.infrastructure.persistence.util.exception.UnsupportedSortConditionException;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import java.util.Arrays;

public enum AuctionSortCondition {

    ID("id", auction.id),
    AUCTIONEER_COUNT("auctioneerCount", auction.auctioneerCount),
    CLOSING_TIME("closingTime", auction.closingTime),
    RELIABILITY("reliability", auction.seller.reliability);

    private final String sortCondition;
    private final ComparableExpressionBase<?> sortExpression;

    AuctionSortCondition(final String sortCondition, final ComparableExpressionBase<?> sortExpression) {
        this.sortCondition = sortCondition;
        this.sortExpression = sortExpression;
    }

    public static OrderSpecifier<?> convert(final Order order) {
        return Arrays.stream(AuctionSortCondition.values())
                     .filter(auctionSortCondition -> isTargetSort(order, auctionSortCondition))
                     .findAny()
                     .map(auctionSortCondition -> convertOrderSpecifier(auctionSortCondition, order))
                     .orElseThrow(() -> new UnsupportedSortConditionException("지원하지 않는 정렬 방식입니다."));
    }

    private static boolean isTargetSort(final Order order, final AuctionSortCondition converter) {
        return converter.sortCondition.equals(order.getProperty());
    }

    private static OrderSpecifier<?> convertOrderSpecifier(
            final AuctionSortCondition sortCondition,
            final Order order
    ) {
        if (order.isDescending()) {
            return sortCondition.sortExpression.desc();
        }

        return sortCondition.sortExpression.asc();
    }
}
