package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadAuctionReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadMultipleAuctionReportResponse(List<AuctionReportInfoResponse> reports) {

    public static ReadMultipleAuctionReportResponse from(final List<ReadAuctionReportDto> auctionReportDtos) {
        final List<AuctionReportInfoResponse> reportResponses = auctionReportDtos.stream()
                                                                                 .map(AuctionReportInfoResponse::from)
                                                                                 .toList();
        return new ReadMultipleAuctionReportResponse(reportResponses);
    }

    public record AuctionReportInfoResponse(
            Long id,
            ReporterResponse reporter,
            LocalDateTime createdTime,
            ReportedAuctionInfoResponse auction,
            String description
    ) {

        private static AuctionReportInfoResponse from(final ReadAuctionReportDto auctionReportDto) {
            return new AuctionReportInfoResponse(
                    auctionReportDto.id(),
                    new ReporterResponse(
                            auctionReportDto.reporterDto().id(),
                            auctionReportDto.reporterDto().name()
                    ),
                    auctionReportDto.createdTime(),
                    new ReportedAuctionInfoResponse(
                            auctionReportDto.auctionDto().id(),
                            auctionReportDto.auctionDto().title()
                    ),
                    auctionReportDto.description()
            );
        }

        public record ReporterResponse(Long id, String name) {
        }

        public record ReportedAuctionInfoResponse(Long id, String title) {
        }
    }
}
