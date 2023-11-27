package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadMultipleAuctionDto;
import com.ddang.ddang.auction.application.dto.response.ReadMultipleAuctionDto.AuctionInfoDto;
import java.util.List;

public record ReadMultipleAuctionResponse(List<AuctionInfoResponse> auctions, boolean isLast) {

    public static ReadMultipleAuctionResponse of(
            final ReadMultipleAuctionDto readMultipleAuctionDto,
            final String imageRelativeUrl
    ) {
        final List<AuctionInfoResponse> auctionInfoRespons =
                readMultipleAuctionDto.auctionInfoDtos()
                                      .stream()
                                      .map(dto -> AuctionInfoResponse.of(dto, imageRelativeUrl))
                                      .toList();

        return new ReadMultipleAuctionResponse(auctionInfoRespons, readMultipleAuctionDto.isLast());
    }

    public record AuctionInfoResponse(
            Long id,
            String title,
            String image,
            int auctionPrice,
            String status,
            int auctioneerCount
    ) {
        public static AuctionInfoResponse of(final AuctionInfoDto dto, final String imageRelativeUrl) {
            return new AuctionInfoResponse(
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
