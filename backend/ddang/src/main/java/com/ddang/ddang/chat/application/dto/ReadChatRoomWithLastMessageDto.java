package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndMessageDto;
import com.ddang.ddang.image.domain.AuctionImage;
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
            final ChatRoomAndMessageDto chatRoomAndMessageDto
    ) {
        final ChatRoom chatRoom = chatRoomAndMessageDto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);
        final Message lastMessage = chatRoomAndMessageDto.message();
        final AuctionImage thumbnailImage = chatRoomAndMessageDto.thumbnailImage();

        return new ReadChatRoomWithLastMessageDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.of(chatRoom.getAuction(), thumbnailImage),
                ReadUserInChatRoomDto.from(partner),
                ReadLastMessageDto.from(lastMessage),
                chatRoom.isChatAvailableTime(LocalDateTime.now())
        );
    }
}
