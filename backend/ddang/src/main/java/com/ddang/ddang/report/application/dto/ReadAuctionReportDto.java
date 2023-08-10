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

    // TODO: 2023/08/08 [고민] ReadAuctionDto 클래스가 같은 이름으로 여러 패키지에 존재함. 이때, import를 할 때 헷갈리거나 실수하지는 않을까요?
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
