package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

public record ReadChatRoomResponse(
        Long id,
        ReadChatPartnerResponse chatPartner,
        ReadAuctionInChatRoomResponse auction,
        boolean isChatAvailable
) {

    public static ReadChatRoomResponse of(
            final ReadParticipatingChatRoomDto chatRoomDto,
            final ImageRelativeUrl profileImageRelativeUrl,
            final ImageRelativeUrl auctionImageRelativeUrl
    ) {
        final ReadChatPartnerResponse chatPartner = ReadChatPartnerResponse.of(
                chatRoomDto.partnerDto(),
                profileImageRelativeUrl
        );
        final ReadAuctionInChatRoomResponse auction = ReadAuctionInChatRoomResponse.of(
                chatRoomDto.auctionDto(),
                auctionImageRelativeUrl
        );

        return new ReadChatRoomResponse(chatRoomDto.id(), chatPartner, auction, chatRoomDto.isChatAvailable());
    }
}
