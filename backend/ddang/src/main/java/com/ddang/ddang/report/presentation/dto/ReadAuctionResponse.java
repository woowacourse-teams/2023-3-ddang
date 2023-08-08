package com.ddang.ddang.report.presentation.dto;

import com.ddang.ddang.report.application.dto.ReadAuctionDto;

public record ReadAuctionResponse(Long id, String title) {

    public static ReadAuctionResponse from(final ReadAuctionDto auctionDto) {
        return new ReadAuctionResponse(auctionDto.id(), auctionDto.title());
    }
}
