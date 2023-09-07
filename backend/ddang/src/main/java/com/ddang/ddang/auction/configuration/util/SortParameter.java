package com.ddang.ddang.auction.configuration.util;

import com.ddang.ddang.auction.configuration.exception.UnsupportedSortParameterException;
import java.util.Arrays;

public enum SortParameter {

    ID("new", "id"),
    AUCTIONEER_COUNT("auctioneer", "auctioneerCount"),
    CLOSING_TIME("closingTime", "closingTime"),
    RELIABILITY("reliability", "reliability");

    private final String sortParameter;
    private final String sortCondition;

    SortParameter(final String sortParameter, final String sortCondition) {
        this.sortParameter = sortParameter;
        this.sortCondition = sortCondition;
    }

    public static String findSortProperty(final String targetSortParameter) {
        if (isDefaultSortCondition(targetSortParameter)) {
            return SortParameter.ID.sortCondition;
        }

        return Arrays.stream(SortParameter.values())
                     .filter(sortParameter -> verifyEquality(targetSortParameter, sortParameter))
                     .map(sortParameter -> sortParameter.sortCondition)
                     .findAny()
                     .orElseThrow(() -> new UnsupportedSortParameterException("지원하지 않는 정렬 방식입니다."));
    }

    private static boolean isDefaultSortCondition(final String targetSortParameter) {
        return targetSortParameter == null || targetSortParameter.isEmpty() || targetSortParameter.isBlank();
    }

    private static boolean verifyEquality(
            final String targetSortParameter,
            final SortParameter sortParameter
    ) {
        return sortParameter.sortParameter.equalsIgnoreCase(targetSortParameter);
    }
}
