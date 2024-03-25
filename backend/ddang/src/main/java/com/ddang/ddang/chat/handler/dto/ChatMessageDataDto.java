package com.ddang.ddang.chat.handler.dto;

public record ChatMessageDataDto(Long chatRoomId, Long receiverId, String contents) {
}
