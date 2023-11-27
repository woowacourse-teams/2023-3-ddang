package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadMultipleAuctionDto;
import com.ddang.ddang.auction.application.dto.response.ReadMultipleAuctionDto.AuctionInfoDto;
import java.util.List;

public record ReadAuctionsResponse(List<ReadAuctionResponse> auctions, boolean isLast) {

    public static ReadAuctionsResponse of(
            final ReadMultipleAuctionDto readMultipleAuctionDto,
            final String imageRelativeUrl
    ) {
        final List<ReadAuctionResponse> readAuctionResponses =
                readMultipleAuctionDto.auctionInfoDtos()
                                      .stream()
                                      .map(dto -> ReadAuctionResponse.of(dto, imageRelativeUrl))
                                      .toList();

        return new ReadAuctionsResponse(readAuctionResponses, readMultipleAuctionDto.isLast());
    }

    public record ReadAuctionResponse(
            Long id,
            String title,
            String image,
            int auctionPrice,
            String status,
            int auctioneerCount
    ) {

        public static ReadAuctionResponse of(final AuctionInfoDto dto, final String imageRelativeUrl) {
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
