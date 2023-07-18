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

        String category,

        String description,

        int startBidPrice,

        Integer lastBidPrice,

        String status,

        int bidUnit,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime registerTime,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime closingTime,

        String directRegions,

        int auctioneerCount,

        boolean deleted
) {

    public static AuctionResponse from(final ReadAuctionDto dto) {
        return new AuctionResponse(
                dto.id(),
                "https://img.danawa.com/prod_img/500000/139/918/img/19918139_1.jpg?_v=20230605093237",
                dto.title(),
                "전자기기",
                dto.description(),
                dto.startBidPrice(),
                dto.lastBidPrice(),
                "unbidden",
                dto.bidUnit(),
                dto.registerTime(),
                dto.closingTime(),
                "서울특별시 송파구 가락1동",
                0,
                dto.deleted()
        );
    }
}
