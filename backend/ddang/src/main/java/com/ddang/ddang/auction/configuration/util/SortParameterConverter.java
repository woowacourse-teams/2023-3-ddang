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
        return Arrays.stream(SortParameterConverter.values())
                .filter(sortParameterConverter -> sortParameterConverter.sortParameter.equalsIgnoreCase(targetSortParameter))
                .map(sortParameterConverter -> sortParameterConverter.sortCondition)
                .findAny()
                .orElseThrow(() -> new UnsupportedSortParameterException("지원하지 않는 정렬 방식입니다."));
    }
}
