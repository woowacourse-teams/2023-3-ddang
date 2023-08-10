package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;

public record ChatRoomResponse(Long id, boolean isChatParticipant) {

    public static ChatRoomResponse from(final ReadChatRoomDto readChatRoomDto) {
        return new ChatRoomResponse(readChatRoomDto.id(), readChatRoomDto.isChatParticipant());
    }
}
