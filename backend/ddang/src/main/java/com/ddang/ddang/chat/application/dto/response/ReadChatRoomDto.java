package com.ddang.ddang.chat.application.dto.response;

public record ReadChatRoomDto(Long id, boolean isChatParticipant) {

    public static ReadChatRoomDto CANNOT_CHAT_DTO = new ReadChatRoomDto(null, false);
}
