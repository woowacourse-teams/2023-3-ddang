package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
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
            final ChatRoom chatRoom,
            final LocalDateTime targetTime
    ) {
        final User partner = chatRoom.calculateChatPartnerOf(findUser);

        return new ReadParticipatingChatRoomDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.from(chatRoom.getAuction()),
                ReadUserInChatRoomDto.from(partner),
                chatRoom.isChatAvailableTime(targetTime)
        );
    }
}
