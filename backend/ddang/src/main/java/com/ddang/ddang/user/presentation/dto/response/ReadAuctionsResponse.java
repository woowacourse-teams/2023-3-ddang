package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;

import java.util.List;

public record ReadAuctionsResponse(List<ReadAuctionResponse> auctions, boolean isLast) {

    public static ReadAuctionsResponse from(final ReadAuctionsDto readAuctionsDto) {
        final List<ReadAuctionResponse> readAuctionResponses = readAuctionsDto.readAuctionDtos()
                                                                              .stream()
                                                                              .map(ReadAuctionResponse::from)
                                                                              .toList();

        return new ReadAuctionsResponse(readAuctionResponses, readAuctionsDto.isLast());
    }
}
