package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;

import java.util.List;

public record ReadAuctionReportsResponse(List<ReadAuctionReportResponse> reports) {

    public static ReadAuctionReportsResponse from(final List<ReadAuctionReportDto> auctionReportDtos) {
        final List<ReadAuctionReportResponse> reportResponses = auctionReportDtos.stream()
                                                                                 .map(ReadAuctionReportResponse::from)
                                                                                 .toList();
        return new ReadAuctionReportsResponse(reportResponses);
    }
}
