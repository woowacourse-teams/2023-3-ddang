package com.ddang.ddang.auction.presentation.dto.request;

import com.ddang.ddang.auction.configuration.util.AuctionSortConditionConsts;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ReadAuctionCondition(
        String sortParameter,

        Long lastAuctionId,

        Integer lastAuctioneerCount,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime lastClosingTime,

        Double lastReliability,

        String title,

        int size
) {

    public static ReadAuctionCondition from(final int size) {
        return new ReadAuctionCondition(null, null, null, null, null, null, size);
    }

    public boolean isFirstPageRequest() {
        return lastAuctionId == null &&
                lastAuctioneerCount == null &&
                lastClosingTime == null &&
                lastReliability == null;
    }

    public boolean isSortByAuctioneerCount() {
        return AuctionSortConditionConsts.AUCTIONEER_COUNT.equals(sortParameter);
    }

    public boolean isSortByClosingTime() {
        return AuctionSortConditionConsts.CLOSING_TINE.equals(sortParameter);
    }

    public boolean isSortByReliability() {
        return AuctionSortConditionConsts.RELIABILITY.equals(sortParameter);
    }
}
