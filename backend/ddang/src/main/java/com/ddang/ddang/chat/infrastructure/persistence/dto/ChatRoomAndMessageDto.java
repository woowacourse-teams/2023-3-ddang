package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;

public record ChatRoomAndMessageDto(ChatRoom chatRoom, Message message) {
}
