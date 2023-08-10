package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.user.domain.User;

import java.time.LocalDateTime;

public record ReadParticipatingChatRoomDto(
        Long id,
        ReadAuctionDto auctionDto,
        ReadUserDto partnerDto,
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
                ReadAuctionDto.from(chatRoom.getAuction()),
                ReadUserDto.from(partner),
                chatRoom.isChatAvailableTime(targetTime)
        );
    }
}
