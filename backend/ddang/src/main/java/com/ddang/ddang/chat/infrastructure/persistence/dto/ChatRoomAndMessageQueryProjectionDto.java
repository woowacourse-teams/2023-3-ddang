package com.ddang.ddang.chat.infrastructure.persistence.dto;

import com.ddang.ddang.chat.domain.ChatRoom;
import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.image.domain.AuctionImage;
import com.querydsl.core.annotations.QueryProjection;

public record ChatRoomAndMessageQueryProjectionDto(ChatRoom chatRoom, Message message, AuctionImage auctionImage) {

    @QueryProjection
    public ChatRoomAndMessageQueryProjectionDto {
    }

    public static ChatRoomAndMessageDto toSortedDto(final ChatRoomAndMessageQueryProjectionDto queryProjectionDto) {
        return new ChatRoomAndMessageDto(
                queryProjectionDto.chatRoom,
                queryProjectionDto.message,
                queryProjectionDto.auctionImage
        );
    }
}
