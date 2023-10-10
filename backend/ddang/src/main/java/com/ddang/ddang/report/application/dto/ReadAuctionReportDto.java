package com.ddang.ddang.report.application.dto;

import com.ddang.ddang.report.domain.AuctionReport;

import java.time.LocalDateTime;

public record ReadAuctionReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadAuctionInReportDto auctionDto,
        String description
) {

    public static ReadAuctionReportDto from(final AuctionReport auctionReport) {
        return new ReadAuctionReportDto(
                auctionReport.getId(),
                ReadReporterDto.from(auctionReport.getReporter()),
                auctionReport.getCreatedTime(),
                ReadAuctionInReportDto.from(auctionReport.getAuction()),
                auctionReport.getDescription()
        );
    }
}
