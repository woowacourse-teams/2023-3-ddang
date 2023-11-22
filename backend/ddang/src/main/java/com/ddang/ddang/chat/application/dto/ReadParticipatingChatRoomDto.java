package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.user.domain.User;

public record ReadParticipatingChatRoomDto(
        Long id,
        ReadAuctionInChatRoomDto auctionDto,
        ReadUserInChatRoomDto partnerDto,
        boolean isChatAvailable
) {

    public static ReadParticipatingChatRoomDto of(
            final User findUser,
            final ChatRoom chatRoom
    ) {
        final User partner = chatRoom.calculateChatPartnerOf(findUser);

        return new ReadParticipatingChatRoomDto(
                chatRoom.getId(),
                ReadAuctionInChatRoomDto.of(chatRoom.getAuction(), chatRoom.getAuction().getThumbnailImageStoreName()),
                ReadUserInChatRoomDto.from(partner),
                chatRoom.isChatAvailablePartner(partner)
        );
    }
}
