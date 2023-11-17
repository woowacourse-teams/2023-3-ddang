package com.ddang.ddang.chat.application.event;

import com.ddang.ddang.chat.domain.ChatRoom;

public record CreateReadMessageLogEvent(ChatRoom chatRoom) {
}
