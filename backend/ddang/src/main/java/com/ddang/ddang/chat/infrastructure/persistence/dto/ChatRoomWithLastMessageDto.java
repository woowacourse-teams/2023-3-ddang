package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;

public record ChatRoomWithLastMessageDto(ChatRoom chatRoom, Message message) {
}
