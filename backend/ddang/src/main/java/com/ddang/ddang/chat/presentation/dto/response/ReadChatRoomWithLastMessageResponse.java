package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadChatRoomWithLastMessageDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

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
            final ImageRelativeUrl profileImageRelativeUrl,
            final ImageRelativeUrl auctionImageRelativeUrl
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
