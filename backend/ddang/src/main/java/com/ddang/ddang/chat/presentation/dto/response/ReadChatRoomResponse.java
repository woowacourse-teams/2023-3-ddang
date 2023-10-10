package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;

public record ReadChatRoomResponse(
        Long id,
        ReadChatPartnerResponse chatPartner,
        ReadAuctionInChatRoomResponse auction,
        boolean isChatAvailable
) {

    public static ReadChatRoomResponse from(final ReadParticipatingChatRoomDto chatRoomDto) {
        final ReadChatPartnerResponse chatPartner = ReadChatPartnerResponse.from(chatRoomDto.partnerDto());
        final ReadAuctionInChatRoomResponse auction = ReadAuctionInChatRoomResponse.from(chatRoomDto.auctionDto());

        return new ReadChatRoomResponse(chatRoomDto.id(), chatPartner, auction, chatRoomDto.isChatAvailable());
    }
}
