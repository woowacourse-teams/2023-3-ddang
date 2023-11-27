package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.dto.MultipleChatRoomInfoDto;
import com.querydsl.core.annotations.QueryProjection;

public record MultipleChatRoomInfoQueryProjectionDto(
        ChatRoom chatRoom,
        Message message,
        Long unreadMessage
) {

    @QueryProjection
    public MultipleChatRoomInfoQueryProjectionDto {
    }

    public MultipleChatRoomInfoDto toDomainDto() {
        return new MultipleChatRoomInfoDto(
                this.chatRoom,
                this.message,
                this.unreadMessage
        );
    }
}
