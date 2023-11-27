package com.ddang.ddang.chat.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadMultipleChatRoomDto(
        Long chatRoomId,
        ReadSimpleAuctionInfoDto auctionDto,
        ReadPartnerInfoDto partnerDto,
        ReadLastMessageDto lastMessageDto,
        long unreadMessageCount,
        boolean isChatAvailable
) {

    public static ReadMultipleChatRoomDto of(
            final User findUser,
            final MultipleChatRoomInfoDto multipleChatRoomInfoDto
    ) {
        final ChatRoom chatRoom = multipleChatRoomInfoDto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);
        final Message lastMessage = multipleChatRoomInfoDto.message();
        final Long unreadMessages = multipleChatRoomInfoDto.unreadMessageCount();

        return new ReadMultipleChatRoomDto(
                chatRoom.getId(),
                ReadSimpleAuctionInfoDto.from(chatRoom.getAuction()),
                ReadPartnerInfoDto.from(partner),
                new ReadLastMessageDto(lastMessage.getCreatedTime(), lastMessage.getContent()),
                unreadMessages,
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

    public record ReadLastMessageDto(LocalDateTime createAd, String content) {
    }
}
