package com.ddang.ddang.chat.application.event;

public record UpdateReadMessageLogEvent(Long readerId, Long chatRoomId, Long lastReadMessageId) {
}
