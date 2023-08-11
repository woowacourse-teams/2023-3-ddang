package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;

public record ReadChatRoomWithLastMessageResponse(
        Long id,
        ReadChatPartnerResponse chatPartner,
        ReadAuctionInChatRoomResponse auction,
        ReadLastMessageResponse lastMessage,
        boolean isChatAvailable
) {

    public static ReadChatRoomWithLastMessageResponse of(final ReadChatRoomWithLastMessageDto dto, final String baseUrl) {
        return new ReadChatRoomWithLastMessageResponse(
                dto.id(),
                ReadChatPartnerResponse.from(dto.partnerDto()),
                ReadAuctionInChatRoomResponse.of(dto.auctionDto(), baseUrl),
                ReadLastMessageResponse.from(dto.lastMessageDto()),
                dto.isChatAvailable()
        );
    }
}
