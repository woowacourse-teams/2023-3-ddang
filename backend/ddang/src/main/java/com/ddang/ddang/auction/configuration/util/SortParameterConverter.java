package com.ddang.ddang.auction.configuration.util;

import com.ddang.ddang.auction.configuration.exception.UnsupportedSortParameterException;
import java.util.Arrays;

public enum SortParameterConverter {

    ID("new", "id"),
    AUCTIONEER_COUNT("auctioneer", "auctioneerCount"),
    CLOSING_TIME("closingTime", "closingTime"),
    RELIABILITY("reliability", "reliability");

    private final String sortParameter;
    private final String sortCondition;

    SortParameterConverter(final String sortParameter, final String sortCondition) {
        this.sortParameter = sortParameter;
        this.sortCondition = sortCondition;
    }

    public static String findSortProperty(final String targetSortParameter) {
        if (isDefaultSortCondition(targetSortParameter)) {
            return SortParameterConverter.ID.sortCondition;
        }

        return Arrays.stream(SortParameterConverter.values())
                     .filter(sortParameterConverter -> verifyEquality(targetSortParameter, sortParameterConverter))
                     .map(sortParameterConverter -> sortParameterConverter.sortCondition)
                     .findAny()
                     .orElseThrow(() -> new UnsupportedSortParameterException("지원하지 않는 정렬 방식입니다."));
    }

    private static boolean isDefaultSortCondition(final String targetSortParameter) {
        return targetSortParameter == null || targetSortParameter.isEmpty() || targetSortParameter.isBlank();
    }

    private static boolean verifyEquality(
            final String targetSortParameter,
            final SortParameterConverter sortParameterConverter
    ) {
        return sortParameterConverter.sortParameter.equalsIgnoreCase(targetSortParameter);
    }
}
