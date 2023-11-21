package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.dto.ChatRoomAndImageDto;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomAndImageQueryProjectionDto(ChatRoom chatRoom) {

    @QueryProjection
    public ChatRoomAndImageQueryProjectionDto {
    }

    public ChatRoomAndImageDto toDto() {
        return new ChatRoomAndImageDto(this.chatRoom, this.chatRoom.getAuction().getThumbnailImageStoreName());
    }
}
