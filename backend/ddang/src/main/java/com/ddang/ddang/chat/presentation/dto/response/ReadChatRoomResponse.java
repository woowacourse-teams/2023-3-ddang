package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.ReadParticipatingChatRoomDto;

public record ReadChatRoomResponse(
        Long id,
        ReadChatPartnerResponse chatPartner,
        ReadAuctionResponse auction,
        boolean isChatAvailable
) {

    public static ReadChatRoomResponse of(final ReadParticipatingChatRoomDto chatRoomDto, final String baseUrl) {
        final ReadChatPartnerResponse chatPartner = ReadChatPartnerResponse.from(chatRoomDto.partnerDto());
        final ReadAuctionResponse auction = ReadAuctionResponse.of(chatRoomDto.auctionDto(), baseUrl);

        return new ReadChatRoomResponse(chatRoomDto.id(), chatPartner, auction, chatRoomDto.isChatAvailable());
    }
}
