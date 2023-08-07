package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.user.domain.User;

public record ReadParticipatingChatRoomDto(
        Long id,
        ReadAuctionDto auctionDto,
        ReadUserDto partnerDto,
        boolean isChatAvailable
) {

    public static ReadParticipatingChatRoomDto of(
            final User partner,
            final ChatRoom chatRoom,
            final boolean isChatAvailable
    ) {
        return new ReadParticipatingChatRoomDto(
                chatRoom.getId(),
                ReadAuctionDto.from(chatRoom.getAuction()),
                ReadUserDto.from(partner),
                isChatAvailable
        );
    }
}
