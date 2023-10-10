package com.ddang.ddang.chat.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;

public record ChatRoomInMessageDto(Long id, Long auctionId, Long buyerId) {

    public static ChatRoomInMessageDto from(final ChatRoom chatRoom) {
        return new ChatRoomInMessageDto(
                chatRoom.getId(),
                chatRoom.getAuction().getId(),
                chatRoom.getBuyer().getId()
        );
    }
}
