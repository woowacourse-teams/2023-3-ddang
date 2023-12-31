package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.ReadAuctionReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ReadAuctionReportResponse(
        Long id,

        ReadReporterResponse reporter,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
        LocalDateTime createdTime,

        ReadAuctionInReportResponse auction,

        String description
) {

    public static ReadAuctionReportResponse from(final ReadAuctionReportDto auctionReportDto) {
        return new ReadAuctionReportResponse(
                auctionReportDto.id(),
                ReadReporterResponse.from(auctionReportDto.reporterDto()),
                auctionReportDto.createdTime(),
                ReadAuctionInReportResponse.from(auctionReportDto.auctionDto()),
                auctionReportDto.description()
        );
    }
}
