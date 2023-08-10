package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.chat.domain.ChatRoom;

import java.util.Optional;

public record ReadChatRoomDto(Long id, boolean isChatParticipant) {

    public static ReadChatRoomDto of(final Optional<ChatRoom> nullableChatRoom, boolean isChatParticipant) {
        return nullableChatRoom.map(room -> new ReadChatRoomDto(room.getId(), isChatParticipant))
                               .orElseGet(() -> new ReadChatRoomDto(null, isChatParticipant));
    }
}
