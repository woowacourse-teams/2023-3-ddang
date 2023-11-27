package com.ddang.ddang.chat.application.dto.response;

import com.ddang.ddang.auction.domain.Auction;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.ddang.ddang.user.domain.User;
import java.time.LocalDateTime;

public record ReadMultipleChatRoomDto(
        Long chatRoomId,
        SimpleAuctionInfoDto auctionDto,
        PartnerInfoDto partnerDto,
        MessageInfoDto lastMessageDto,
        long unreadMessageCount,
        boolean isChatAvailable
) {

    public static ReadMultipleChatRoomDto of(final User findUser, final MultipleChatRoomInfoDto dto) {
        final ChatRoom chatRoom = dto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);
        final Message lastMessage = dto.message();
        final Long unreadMessages = dto.unreadMessageCount();

        return new ReadMultipleChatRoomDto(
                chatRoom.getId(),
                SimpleAuctionInfoDto.from(chatRoom.getAuction()),
                PartnerInfoDto.from(partner),
                new MessageInfoDto(lastMessage.getCreatedTime(), lastMessage.getContent()),
                unreadMessages,
                chatRoom.isChatAvailablePartner(partner)
        );
    }

    public record SimpleAuctionInfoDto(
            Long id,
            String title,
            Integer lastBidPrice,
            String thumbnailImageStoreName
    ) {

        public static SimpleAuctionInfoDto from(final Auction auction) {
            return new SimpleAuctionInfoDto(
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

    public record MessageInfoDto(LocalDateTime createdTime, String content) {
    }
}
