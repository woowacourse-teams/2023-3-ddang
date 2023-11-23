package com.ddang.ddang.chat.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.user.domain.User;

public record ReadSingleChatRoomDto(
        Long id,
        ReadSimpleAuctionInfoDto auctionDto,
        ReadPartnerInfoDto partnerDto,
        boolean isChatAvailable
) {

    public static ReadSingleChatRoomDto of(
            final User findUser,
            final ChatRoom chatRoom
    ) {
        final User partner = chatRoom.calculateChatPartnerOf(findUser);

        return new ReadSingleChatRoomDto(
                chatRoom.getId(),
                ReadSimpleAuctionInfoDto.from(chatRoom.getAuction()),
                ReadPartnerInfoDto.from(partner),
                chatRoom.isChatAvailablePartner(partner)
        );
    }

    public record ReadSimpleAuctionInfoDto(
            Long id,
            String title,
            Integer lastBidPrice,
            String thumbnailImageStoreName
    ) {

        private static ReadSimpleAuctionInfoDto from(final Auction auction) {
            return new ReadSimpleAuctionInfoDto(
                    auction.getId(),
                    auction.getTitle(),
                    auction.findLastBid().map(lastBid -> lastBid.getPrice().getValue()).orElse(null),
                    auction.getThumbnailImageStoreName()
            );
        }
    }

    public record ReadPartnerInfoDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        private static ReadPartnerInfoDto from(final User user) {
            return new ReadPartnerInfoDto(
                    user.getId(),
                    user.findName(),
                    user.getProfileImageStoreName(),
                    user.getReliability().getValue(),
                    user.isDeleted()
            );
        }
    }
}
