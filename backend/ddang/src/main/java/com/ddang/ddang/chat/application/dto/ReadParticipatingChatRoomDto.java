package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.infrastructure.persistence.dto.ChatRoomAndImageDto;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadParticipatingChatRoomDto(
        Long id,
        ReadAuctionInChatRoomDto auctionDto,
        ReadUserInChatRoomDto partnerDto,
        boolean isChatAvailable
) {

    public static ReadParticipatingChatRoomDto of(
            final User findUser,
            final ChatRoomAndImageDto chatRoomAndImageDto,
            final LocalDateTime targetTime
    ) {
        final ChatRoom chatRoom = chatRoomAndImageDto.chatRoom();
        final User partner = chatRoom.calculateChatPartnerOf(findUser);

        return new ReadParticipatingChatRoomDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.of(chatRoom.getAuction(), chatRoomAndImageDto.thumbnailImage()),
                ReadUserInChatRoomDto.from(partner),
                chatRoom.isChatAvailableTime(targetTime)
        );
    }
}
