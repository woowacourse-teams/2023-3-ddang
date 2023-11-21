package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import java.util.List;

public record ReadAuctionsResponse(List<ReadAuctionResponse> auctions, boolean isLast) {

    public static ReadAuctionsResponse of(
            final ReadAuctionsDto readAuctionsDto,
            final String imageRelativeUrl
    ) {
        final List<ReadAuctionResponse> readAuctionResponses =
                readAuctionsDto.readAuctionDtos()
                               .stream()
                               .map(dto -> ReadAuctionResponse.of(dto, imageRelativeUrl))
                               .toList();

        return new ReadAuctionsResponse(readAuctionResponses, readAuctionsDto.isLast());
    }
}
