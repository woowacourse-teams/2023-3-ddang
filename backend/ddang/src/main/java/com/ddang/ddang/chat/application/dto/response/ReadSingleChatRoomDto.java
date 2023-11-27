package com.ddang.ddang.chat.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.user.domain.User;

public record ReadSingleChatRoomDto(
        Long id,
        DetailAuctionInfoDto auctionDto,
        PartnerInfoDto partnerDto,
        boolean isChatAvailable
) {

    public static ReadSingleChatRoomDto of(final User findUser, final ChatRoom chatRoom) {
        final User partner = chatRoom.calculateChatPartnerOf(findUser);

        return new ReadSingleChatRoomDto(
                chatRoom.getId(),
                DetailAuctionInfoDto.from(chatRoom.getAuction()),
                PartnerInfoDto.from(partner),
                chatRoom.isChatAvailablePartner(partner)
        );
    }

    public record DetailAuctionInfoDto(
            Long id,
            String title,
            Integer lastBidPrice,
            String thumbnailImageStoreName
    ) {

        public static DetailAuctionInfoDto from(final Auction auction) {
            return new DetailAuctionInfoDto(
                    auction.getId(),
                    auction.getTitle(),
                    auction.findLastBid().map(lastBid -> lastBid.getPrice().getValue()).orElse(null),
                    auction.getThumbnailImageStoreName()
            );
        }
    }

    public record PartnerInfoDto(
            Long id,
            String name,
            String profileImageStoreName,
            double reliability,
            boolean isDeleted
    ) {

        public static PartnerInfoDto from(final User user) {
            return new PartnerInfoDto(
                    user.getId(),
                    user.findName(),
                    user.getProfileImageStoreName(),
                    user.getReliability().getValue(),
                    user.isDeleted()
            );
        }
    }
}
