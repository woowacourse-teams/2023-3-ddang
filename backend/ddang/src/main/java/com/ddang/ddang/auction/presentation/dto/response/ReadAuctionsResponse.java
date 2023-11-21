package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
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

    public record ReadAuctionResponse(
            Long id,
            String title,
            String image,
            int auctionPrice,
            String status,
            int auctioneerCount
    ) {
        private static ReadAuctionResponse of(final ReadAuctionDto dto, final String imageRelativeUrl) {
            return new ReadAuctionResponse(
                    dto.id(),
                    dto.title(),
                    imageRelativeUrl + dto.auctionImageStoreNames().get(0),
                    processAuctionPrice(dto.startPrice(), dto.lastBidPrice()),
                    dto.auctionStatus().name(),
                    dto.auctioneerCount()
            );
        }

        private static int processAuctionPrice(final Integer startPrice, final Integer lastBidPrice) {
            if (lastBidPrice == null) {
                return startPrice;
            }

            return lastBidPrice;
        }
    }
}
