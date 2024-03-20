package com.ddang.ddang.chat.application.event;

import com.ddang.ddang.chat.domain.Message;

public record UpdateReadMessageLogEvent(Long readerId, Long chatRoomId, Message lastReadMessage) {
}
