package com.ddang.ddang.auction.presentation.dto;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuctionResponse(
        Long id,

        String images,

        String title,

        CategoryResponse category,

        String description,

        int startBidPrice,

        Integer lastBidPrice,

        String status,

        int bidUnit,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime registerTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime closingTime,

        DirectRegionResponse directRegions,

        int auctioneerCount,

        boolean deleted
) {

    public static AuctionResponse from(final ReadAuctionDto dto) {
        // TODO 2차 데모데이 이후 리펙터링 예정
        return new AuctionResponse(
                dto.id(),
                dto.image(),
                dto.title(),
                new CategoryResponse(dto.mainCategory(), dto.subCategory()),
                dto.description(),
                dto.startBidPrice(),
                dto.lastBidPrice(),
                "unbidden",
                dto.bidUnit(),
                dto.registerTime(),
                dto.closingTime(),
                new DirectRegionResponse(dto.firstRegion(), dto.secondRegion(), dto.thirdRegion()),
                0,
                dto.deleted()
        );
    }
}
