package com.ddang.ddang.auction.presentation.dto.response;

import java.util.List;

public record ReadAuctionsResponse(List<ReadAuctionResponse> auctions, Long lastAuctionId) {
}
