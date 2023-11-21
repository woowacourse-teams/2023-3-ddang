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

    public static ReadChatRoomWithLastMessageResponse of(
            final ReadChatRoomWithLastMessageDto dto,
            final String profileImageRelativeUrl,
            final String auctionImageRelativeUrl
    ) {
        return new ReadChatRoomWithLastMessageResponse(
                dto.id(),
                ReadChatPartnerResponse.of(dto.partnerDto(), profileImageRelativeUrl),
                ReadAuctionInChatRoomResponse.of(dto.auctionDto(), auctionImageRelativeUrl),
                ReadLastMessageResponse.from(dto.lastMessageDto()),
                dto.unreadMessageCount(),
                dto.isChatAvailable()
        );
    }
}
