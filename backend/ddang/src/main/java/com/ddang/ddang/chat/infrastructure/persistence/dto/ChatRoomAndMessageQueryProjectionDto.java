package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomAndMessageQueryProjectionDto(ChatRoom chatRoom, Message message) {
    @QueryProjection
    public ChatRoomAndMessageQueryProjectionDto {
    }
}
