package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadChatRoomWithLastMessageDto(
        Long id,
        ReadAuctionInChatRoomDto auctionDto,
        ReadUserInChatRoomDto partnerDto,
        ReadLastMessageDto lastMessageDto,
        boolean isChatAvailable
) {

    public static ReadChatRoomWithLastMessageDto of(
            final User findUser,
            final ChatRoom chatRoom,
            final Message lastMessage
    ) {
        final User partner = chatRoom.calculateChatPartnerOf(findUser);

        return new ReadChatRoomWithLastMessageDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.from(chatRoom.getAuction()),
                ReadUserInChatRoomDto.from(partner),
                ReadLastMessageDto.from(lastMessage),
                chatRoom.isChatAvailableTime(LocalDateTime.now())
        );
    }
}
