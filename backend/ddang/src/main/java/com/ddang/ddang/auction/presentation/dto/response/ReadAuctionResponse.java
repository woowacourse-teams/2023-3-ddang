package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import java.time.LocalDateTime;

public record ReadAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static ReadAuctionResponse of(final ReadAuctionDto dto, final String baseUrl) {
        return new ReadAuctionResponse(
                dto.id(),
                dto.title(),
                convertImageUrl(dto, baseUrl),
                processAuctionPrice(dto.startPrice(), dto.lastBidPrice()),
                processAuctionStatus(dto.closingTime(), dto.lastBidPrice()),
                0
        );
    }

    private static String convertImageUrl(final ReadAuctionDto dto, final String baseUrl) {
        return baseUrl.concat(String.valueOf(dto.auctionImageIds().get(0)));
    }

    private static int processAuctionPrice(final Integer startPrice, final Integer lastBidPrice) {
        if (lastBidPrice == null) {
            return startPrice;
        }

        return lastBidPrice;
    }

    // TODO 2차 데모데이 이후 enum으로 처리
    private static String processAuctionStatus(final LocalDateTime closingTime, final Integer lastBidPrice) {
        if (LocalDateTime.now().isBefore(closingTime) && lastBidPrice == null) {
            return "UNBIDDEN";
        }
        if (LocalDateTime.now().isBefore(closingTime) && lastBidPrice != null) {
            return "ONGOING";
        }
        if (LocalDateTime.now().isAfter(closingTime) && lastBidPrice == null) {
            return "FAILURE";
        }
        return "SUCCESS";
    }
}
