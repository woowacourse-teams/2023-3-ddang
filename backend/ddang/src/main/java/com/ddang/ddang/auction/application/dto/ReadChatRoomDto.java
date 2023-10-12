package com.ddang.ddang.auction.application.dto;

public record ReadChatRoomDto(Long id, boolean isChatParticipant) {

    public static ReadChatRoomDto CANNOT_CHAT_DTO = new ReadChatRoomDto(null, false);
}
