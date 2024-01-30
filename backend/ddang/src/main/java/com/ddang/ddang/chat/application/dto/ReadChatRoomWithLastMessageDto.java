package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.user.domain.User;

public record ReadChatRoomWithLastMessageDto(
        Long id,
        ReadAuctionInChatRoomDto auctionDto,
        ReadUserInChatRoomDto partnerDto,
        ReadLastMessageDto lastMessageDto,
        Long unreadMessageCount,
        boolean isChatAvailable
) {

    public static ReadChatRoomWithLastMessageDto of(
            final User findUser,
            final ChatRoomAndMessageAndImageDto chatRoomAndMessageAndImageDto
    ) {
        final ChatRoom chatRoom = chatRoomAndMessageAndImageDto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);
        final Message lastMessage = chatRoomAndMessageAndImageDto.message();
        final AuctionImage thumbnailImage = chatRoomAndMessageAndImageDto.thumbnailImage();
        final Long unreadMessages = chatRoomAndMessageAndImageDto.unreadMessageCount();

        return new ReadChatRoomWithLastMessageDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.of(chatRoom.getAuction(), thumbnailImage),
                ReadUserInChatRoomDto.from(partner),
                ReadLastMessageDto.from(lastMessage),
                unreadMessages,
                chatRoom.isChatAvailablePartner(partner)
        );
    }
}
