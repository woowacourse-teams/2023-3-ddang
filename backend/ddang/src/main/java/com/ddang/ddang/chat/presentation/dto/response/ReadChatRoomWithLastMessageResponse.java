package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;

public record ReadChatRoomWithLastMessageResponse(
        Long id,
        ReadChatPartnerResponse chatPartner,
        ReadAuctionInChatRoomResponse auction,
        ReadLastMessageResponse lastMessage,
        Long unreadMessageCount,
        boolean isChatAvailable
) {

    public static ReadChatRoomWithLastMessageResponse from(final ReadChatRoomWithLastMessageDto dto) {
        return new ReadChatRoomWithLastMessageResponse(
                dto.id(),
                ReadChatPartnerResponse.from(dto.partnerDto()),
                ReadAuctionInChatRoomResponse.from(dto.auctionDto()),
                ReadLastMessageResponse.from(dto.lastMessageDto()),
                dto.unreadMessageCount(),
                dto.isChatAvailable()
        );
    }
}
