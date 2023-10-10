package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadAuctionInReportDto;

public record ReadAuctionInReportResponse(Long id, String title) {

    public static ReadAuctionInReportResponse from(final ReadAuctionInReportDto auctionDto) {
        return new ReadAuctionInReportResponse(auctionDto.id(), auctionDto.title());
    }
}
