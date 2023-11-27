package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.report.domain.AuctionReport;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadAuctionReportDto(
        Long id,
        ReporterInfoDto reporterDto,
        LocalDateTime createdTime,
        ReportedAuctionInfoDto auctionDto,
        String description
) {

    public static ReadAuctionReportDto from(final AuctionReport auctionReport) {
        return new ReadAuctionReportDto(
                auctionReport.getId(),
                ReporterInfoDto.from(auctionReport.getReporter()),
                auctionReport.getCreatedTime(),
                ReportedAuctionInfoDto.from(auctionReport.getAuction()),
                auctionReport.getDescription()
        );
    }

    public record ReporterInfoDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        public static ReporterInfoDto from(final User reporter) {
            return new ReporterInfoDto(
                    reporter.getId(),
                    reporter.findName(),
                    reporter.getProfileImageStoreName(),
                    reporter.getReliability().getValue(),
                    reporter.isDeleted()
            );
        }
    }

    public record ReportedAuctionInfoDto(
            Long id,
            SellerInfoDto seller,
            String title,
            String description,
            int bidUnit,
            int startPrice,
            boolean deleted,
            LocalDateTime closingTime,
            int auctioneerCount
    ) {

        public static ReportedAuctionInfoDto from(final Auction auction) {
            return new ReportedAuctionInfoDto(
                    auction.getId(),
                    SellerInfoDto.from(auction.getSeller()),
                    auction.getTitle(),
                    auction.getDescription(),
                    auction.getBidUnit().getValue(),
                    auction.getStartPrice().getValue(),
                    auction.isDeleted(),
                    auction.getClosingTime(),
                    auction.getAuctioneerCount()
            );
        }

        public record SellerInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            public static SellerInfoDto from(final User user) {
                return new SellerInfoDto(
                        user.getId(),
                        user.findName(),
                        user.getProfileImageStoreName(),
                        user.getReliability().getValue(),
                        user.getOauthInformation().getOauthId(),
                        user.isDeleted()
                );
            }
        }
    }
}
