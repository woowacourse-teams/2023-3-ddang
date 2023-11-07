package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.domain.dto.ChatRoomAndMessageAndImageDto;
import com.ddang.ddang.image.domain.AuctionImage;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomAndMessageAndImageQueryProjectionDto(
        ChatRoom chatRoom,
        Message message,
        AuctionImage auctionImage,
        Long unreadMessage
) {

    @QueryProjection
    public ChatRoomAndMessageAndImageQueryProjectionDto {
    }

    public ChatRoomAndMessageAndImageDto toSortedDto() {
        return new ChatRoomAndMessageAndImageDto(
                this.chatRoom,
                this.message,
                this.auctionImage,
                this.unreadMessage
        );
    }
}
