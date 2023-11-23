package com.ddang.ddang.report.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.report.domain.ChatRoomReport;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadChatRoomReportDto(
        Long id,
        ReadReporterDto reporterDto,
        LocalDateTime createdTime,
        ReadReportTargetChatRoomInfoDto chatRoomDto,
        String description
) {

    public static ReadChatRoomReportDto from(final ChatRoomReport chatRoomReport) {
        return new ReadChatRoomReportDto(
                chatRoomReport.getId(),
                ReadReporterDto.from(chatRoomReport.getReporter()),
                chatRoomReport.getCreatedTime(),
                ReadReportTargetChatRoomInfoDto.from(chatRoomReport.getChatRoom()),
                chatRoomReport.getDescription()
        );
    }

    public record ReadReporterDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        private static ReadReporterDto from(final User reporter) {
            return new ReadReporterDto(
                    reporter.getId(),
                    reporter.findName(),
                    reporter.getProfileImageStoreName(),
                    reporter.getReliability().getValue(),
                    reporter.isDeleted()
            );
        }
    }

    public record ReadReportTargetChatRoomInfoDto(
            Long id,
            ReadReportTargetAuctionInfoDto auctionDto,
            ReadPartnerInfoDto partnerDto
    ) {

        private static ReadReportTargetChatRoomInfoDto from(final ChatRoom chatRoom) {
            return new ReadReportTargetChatRoomInfoDto(
                    chatRoom.getId(),
                    ReadReportTargetAuctionInfoDto.from(chatRoom.getAuction()),
                    ReadPartnerInfoDto.from(chatRoom.getBuyer())
            );
        }

        public record ReadPartnerInfoDto(
                Long id,
                String name,
                String profileImageStoreName,
                double reliability,
                String oauthId,
                boolean isSellerDeleted
        ) {

            private static ReadPartnerInfoDto from(final User user) {
                return new ReadPartnerInfoDto(
                        user.getId(),
                        user.findName(),
                        user.getProfileImageStoreName(),
                        user.getReliability().getValue(),
                        user.getOauthInformation().getOauthId(),
                        user.isDeleted()
                );
            }
        }

        public record ReadReportTargetAuctionInfoDto(
                Long id,
                ReadSellerInfoDto seller,
                String title,
                String description,
                int bidUnit,
                int startPrice,
                boolean deleted,
                LocalDateTime closingTime,
                int auctioneerCount
        ) {

            private static ReadReportTargetAuctionInfoDto from(final Auction auction) {
                return new ReadReportTargetAuctionInfoDto(
                        auction.getId(),
                        ReadSellerInfoDto.from(auction.getSeller()),
                        auction.getTitle(),
                        auction.getDescription(),
                        auction.getBidUnit().getValue(),
                        auction.getStartPrice().getValue(),
                        auction.isDeleted(),
                        auction.getClosingTime(),
                        auction.getAuctioneerCount()
                );
            }

            public record ReadSellerInfoDto(
                    Long id,
                    String name,
                    String profileImageStoreName,
                    double reliability,
                    String oauthId,
                    boolean isSellerDeleted
            ) {

                private static ReadSellerInfoDto from(final User user) {
                    return new ReadReportTargetAuctionInfoDto.ReadSellerInfoDto(
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
