package com.ddang.ddang.report.presentation.dto.response;

import com.ddang.ddang.report.application.dto.response.ReadAuctionReportDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

public record ReadAuctionReportsResponse(List<ReadAuctionReportResponse> reports) {

    public static ReadAuctionReportsResponse from(final List<ReadAuctionReportDto> auctionReportDtos) {
        final List<ReadAuctionReportResponse> reportResponses = auctionReportDtos.stream()
                                                                                 .map(ReadAuctionReportResponse::from)
                                                                                 .toList();
        return new ReadAuctionReportsResponse(reportResponses);
    }

    public record ReadAuctionReportResponse(
            Long id,

            ReadReporterResponse reporter,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
            LocalDateTime createdTime,

            ReadReportTargetAuctionSimpleInfoResponse auction,

            String description
    ) {

        private static ReadAuctionReportResponse from(final ReadAuctionReportDto auctionReportDto) {
            return new ReadAuctionReportResponse(
                    auctionReportDto.id(),
                    new ReadReporterResponse(
                            auctionReportDto.reporterDto().id(),
                            auctionReportDto.reporterDto().name()
                    ),
                    auctionReportDto.createdTime(),
                    new ReadReportTargetAuctionSimpleInfoResponse(
                            auctionReportDto.auctionDto().id(),
                            auctionReportDto.auctionDto().title()
                    ),
                    auctionReportDto.description()
            );
        }

        public record ReadReporterResponse(Long id, String name) {
        }

        public record ReadReportTargetAuctionSimpleInfoResponse(Long id, String title) {
        }
    }
}
