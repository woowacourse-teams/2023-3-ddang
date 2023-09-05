package com.ddang.ddang.auction.configuration.util;

import com.ddang.ddang.auction.configuration.exception.UnsupportedSortTypeException;
import java.util.Arrays;

public enum SortPropertyConverter {

    ID("new", "id"),
    AUCTIONEER_COUNT("auctioneer", "auctioneerCount"),
    CLOSING_TIME("closingTime", "closingTime"),
    RELIABILITY("reliability", "reliability");

    private final String sortType;
    private final String sortProperty;

    SortPropertyConverter(final String sortParameter, final String sortProperty) {
        this.sortType = sortParameter;
        this.sortProperty = sortProperty;
    }

    public static String findSortProperty(final String targetSortParameter) {
        return Arrays.stream(SortPropertyConverter.values())
                .filter(sortPropertyConverter -> sortPropertyConverter.sortType.equalsIgnoreCase(targetSortParameter))
                .map(sortPropertyConverter -> sortPropertyConverter.sortProperty)
                .findAny()
                .orElseThrow(() -> new UnsupportedSortTypeException("지원하지 않는 정렬 방식입니다."));
    }
}
