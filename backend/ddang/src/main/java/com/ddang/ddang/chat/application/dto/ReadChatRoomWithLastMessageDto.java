package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageQueryProjectionDto;
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
            final ChatRoomAndMessageQueryProjectionDto chatRoomAndMessageQueryProjectionDto
            ) {
        final ChatRoom chatRoom = chatRoomAndMessageQueryProjectionDto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);
        final Message lastMessage = chatRoomAndMessageQueryProjectionDto.message();

        return new ReadChatRoomWithLastMessageDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.from(chatRoom.getAuction()),
                ReadUserInChatRoomDto.from(partner),
                ReadLastMessageDto.from(lastMessage),
                chatRoom.isChatAvailableTime(LocalDateTime.now())
        );
    }
}
