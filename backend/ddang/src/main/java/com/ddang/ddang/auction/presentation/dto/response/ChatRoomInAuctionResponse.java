package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;

public record ChatRoomInAuctionResponse(Long id, boolean isChatParticipant) {

    public static ChatRoomInAuctionResponse from(final ReadChatRoomDto readChatRoomDto) {

        return new ChatRoomInAuctionResponse(readChatRoomDto.id(), readChatRoomDto.isChatParticipant());
    }
}
