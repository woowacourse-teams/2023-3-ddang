package com.ddang.ddang.chat.domain.dto;

import com.ddang.ddang.chat.domain.ChatRoom;

public record ChatRoomAndImageDto(ChatRoom chatRoom, String thumbnailImageStoreName) {
}
