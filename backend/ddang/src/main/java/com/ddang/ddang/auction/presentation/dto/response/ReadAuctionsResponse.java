package com.ddang.ddang.auction.presentation.dto.response;

import java.util.List;

public record ReadAuctionsResponse(List<ReadAuctionResponse> auctions, boolean isLast) {

    public static ReadAuctionsResponse of(final List<ReadAuctionResponse> auctions, final int size) {
        if (auctions.size() > size) {
            return new ReadAuctionsResponse(auctions, false);
        }
        return new ReadAuctionsResponse(auctions, true);
    }
}
