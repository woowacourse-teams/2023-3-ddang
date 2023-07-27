package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record AuctionDetailResponse(
        Long id,

        List<String> images,

        String title,

        CategoryResponse category,

        String description,

        int startPrice,

        Integer lastBidPrice,

        String status,

        int bidUnit,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime registerTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime closingTime,

        List<DirectRegionsResponse> directRegions,

        int auctioneerCount
) {

    public static AuctionDetailResponse from(final ReadAuctionDto dto) {
        // TODO 2차 데모데이 이후 리펙터링 예정
        return new AuctionDetailResponse(
                dto.id(),
                List.of(dto.image()),
                dto.title(),
                new CategoryResponse(dto.mainCategory(), dto.subCategory()),
                dto.description(),
                dto.startPrice(),
                dto.lastBidPrice(),
                processAuctionStatus(dto.closingTime(), dto.lastBidPrice()),
                dto.bidUnit(),
                dto.registerTime(),
                dto.closingTime(),
                convertDirectRegionsResponse(dto),
                0
        );
    }

    private static List<DirectRegionsResponse> convertDirectRegionsResponse(final ReadAuctionDto dto) {
        return dto.auctionRegions()
                  .stream()
                  .map(DirectRegionsResponse::from)
                  .toList();
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
