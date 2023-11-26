package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadMultipleAuctionDto;
import java.util.List;

public record ReadMultipleAuctionResponse(List<ReadAuctionResponse> auctions, boolean isLast) {

    public static ReadMultipleAuctionResponse of(
            final ReadMultipleAuctionDto readMultipleAuctionDto,
            final String imageRelativeUrl
    ) {
        final List<ReadAuctionResponse> readAuctionResponses =
                readMultipleAuctionDto.readAuctionDtos()
                                      .stream()
                                      .map(dto -> ReadAuctionResponse.of(dto, imageRelativeUrl))
                                      .toList();

        return new ReadMultipleAuctionResponse(readAuctionResponses, readMultipleAuctionDto.isLast());
    }

    public record ReadAuctionResponse(
            Long id,
            String title,
            String image,
            int auctionPrice,
            String status,
            int auctioneerCount
    ) {
        private static ReadAuctionResponse of(final ReadMultipleAuctionDto.ReadAuctionDto dto, final String imageRelativeUrl) {
            return new ReadAuctionResponse(
                    dto.id(),
                    dto.title(),
                    imageRelativeUrl + dto.auctionThumbnailImageStoreName(),
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
