package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadChatRoomReportDto(
        Long id,
        ReporterInfoDto reporterDto,
        LocalDateTime createdTime,
        ReportedChatRoomInfoDto chatRoomDto,
        String description
) {

    public static ReadChatRoomReportDto from(final ChatRoomReport chatRoomReport) {
        return new ReadChatRoomReportDto(
                chatRoomReport.getId(),
                ReporterInfoDto.from(chatRoomReport.getReporter()),
                chatRoomReport.getCreatedTime(),
                ReportedChatRoomInfoDto.from(chatRoomReport.getChatRoom()),
                chatRoomReport.getDescription()
        );
    }

    public record ReporterInfoDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        private static ReporterInfoDto from(final User reporter) {
            return new ReporterInfoDto(
                    reporter.getId(),
                    reporter.findName(),
                    reporter.getProfileImageStoreName(),
                    reporter.getReliability().getValue(),
                    reporter.isDeleted()
            );
        }
    }

    public record ReportedChatRoomInfoDto(
            Long id,
            SimpleAuctionInfoDto auctionDto,
            PartnerInfoDto partnerDto
    ) {

        public static ReportedChatRoomInfoDto from(final ChatRoom chatRoom) {
            return new ReportedChatRoomInfoDto(
                    chatRoom.getId(),
                    SimpleAuctionInfoDto.from(chatRoom.getAuction()),
                    PartnerInfoDto.from(chatRoom.getBuyer())
            );
        }

        public record PartnerInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            public static PartnerInfoDto from(final User user) {
                return new PartnerInfoDto(
                        user.getId(),
                        user.findName(),
                        user.getProfileImageStoreName(),
                        user.getReliability().getValue(),
                        user.getOauthInformation().getOauthId(),
                        user.isDeleted()
                );
            }
        }

        public record SimpleAuctionInfoDto(
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

            public static SimpleAuctionInfoDto from(final Auction auction) {
                return new SimpleAuctionInfoDto(
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

}
