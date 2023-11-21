package com.ddang.ddang.user.presentation.dto.response;

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

        List<DirectRegionResponse> directRegions,

        int auctioneerCount
) {

    public static AuctionDetailResponse of(final ReadAuctionDto dto, final String imageRelativeUrl) {
        return new AuctionDetailResponse(
                dto.id(),
                convertImageFullUrls(dto, imageRelativeUrl),
                dto.title(),
                new CategoryResponse(dto.mainCategory(), dto.subCategory()),
                dto.description(),
                dto.startPrice(),
                dto.lastBidPrice(),
                dto.auctionStatus().name(),
                dto.bidUnit(),
                dto.registerTime(),
                dto.closingTime(),
                convertDirectRegionsResponse(dto),
                dto.auctioneerCount()
        );
    }

    private static List<String> convertImageFullUrls(final ReadAuctionDto dto, final String imageRelativeUrl) {
        return dto.auctionImageStoreNames()
                  .stream()
                  .map(storeName -> imageRelativeUrl + storeName)
                  .toList();
    }

    private static List<DirectRegionResponse> convertDirectRegionsResponse(final ReadAuctionDto dto) {
        return dto.auctionRegions()
                  .stream()
                  .map(DirectRegionResponse::from)
                  .toList();
    }
}
