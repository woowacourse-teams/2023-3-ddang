package com.ddang.ddang.chat.domain.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;

public record ChatRoomAndMessageAndImageDto(
        ChatRoom chatRoom,
        Message message,
        String thumbnailImageStoreName,
        Long unreadMessageCount
) {
}
