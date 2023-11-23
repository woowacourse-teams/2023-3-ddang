package com.ddang.ddang.chat.application.dto.request;

import com.ddang.ddang.chat.presentation.dto.request.CreateChatRoomRequest;

public record CreateChatRoomDto(Long auctionId) {

    public static CreateChatRoomDto from(final CreateChatRoomRequest chatRoomRequest) {
        return new CreateChatRoomDto(chatRoomRequest.auctionId());
    }
}
