package com.ddang.ddang.chat.presentation.dto.request;

public record ReadMessageRequest(Long messageReaderId, Long chatRoomId, Long lastMessageId) {
}
