package com.ddang.ddang.chat.domain.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;

public record MultipleChatRoomInfoDto(
        ChatRoom chatRoom,
        Message message,
        Long unreadMessageCount
) {
}
