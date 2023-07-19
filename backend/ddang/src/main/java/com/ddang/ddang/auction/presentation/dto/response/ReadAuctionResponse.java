package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;

import java.time.LocalDateTime;

// TODO 2차 데모데이 이후 리팩토링 예정
public record ReadAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static ReadAuctionResponse from(final ReadAuctionDto dto) {
        return new ReadAuctionResponse(
                dto.id(),
                dto.title(),
                dto.image(),
                processAuctionPrice(dto.startBidPrice(), dto.lastBidPrice()),
                processAuctionStatus(dto.closingTime(), dto.lastBidPrice()),
                0
        );
    }

    private static int processAuctionPrice(final Integer startBidPrice, final Integer lastBidPrice) {
        if (lastBidPrice == null) {
            return startBidPrice;
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
