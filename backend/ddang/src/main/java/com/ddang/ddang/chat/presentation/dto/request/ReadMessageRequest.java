package com.ddang.ddang.chat.presentation.dto.request;

public record ReadMessageRequest(Long userId, Long chatRoomId, Long lastMessageId) {
}
