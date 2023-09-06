package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomWithLastMessageDto;
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
            final ChatRoomWithLastMessageDto chatRoomWithLastMessageDto
            ) {
        final ChatRoom chatRoom = chatRoomWithLastMessageDto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);
        final Message lastMessage = chatRoomWithLastMessageDto.message();

        return new ReadChatRoomWithLastMessageDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.from(chatRoom.getAuction()),
                ReadUserInChatRoomDto.from(partner),
                ReadLastMessageDto.from(lastMessage),
                chatRoom.isChatAvailableTime(LocalDateTime.now())
        );
    }
}
