package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.image.domain.AuctionImage;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomAndImageQueryProjectionDto(ChatRoom chatRoom, AuctionImage auctionImage) {

    @QueryProjection
    public ChatRoomAndImageQueryProjectionDto {
    }

    public ChatRoomAndImageDto toDto() {
        return new ChatRoomAndImageDto(this.chatRoom, this.auctionImage);
    }
}
