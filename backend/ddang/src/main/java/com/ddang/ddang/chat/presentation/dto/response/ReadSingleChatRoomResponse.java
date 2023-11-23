package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.response.ReadSingleChatRoomDto;

public record ReadSingleChatRoomResponse(
        Long id,
        ReadPartnerResponse partner,
        ReadChatRoomAuctionInfoResponse auction,
        boolean isChatAvailable
) {

    public static ReadSingleChatRoomResponse of(
            final ReadSingleChatRoomDto chatRoomDto,
            final String profileImageRelativeUrl,
            final String auctionImageRelativeUrl
    ) {
        final ReadPartnerResponse chatPartner = ReadPartnerResponse.of(chatRoomDto, profileImageRelativeUrl);
        final ReadChatRoomAuctionInfoResponse auction = ReadChatRoomAuctionInfoResponse.of(chatRoomDto, auctionImageRelativeUrl);

        return new ReadSingleChatRoomResponse(chatRoomDto.id(), chatPartner, auction, chatRoomDto.isChatAvailable());
    }

    public record ReadPartnerResponse(Long id, String name, String profileImage) {

        private static ReadPartnerResponse of(final ReadSingleChatRoomDto dto, final String imageRelativeUrl) {
            return new ReadPartnerResponse(
                    dto.partnerDto().id(),
                    dto.partnerDto().name(),
                    imageRelativeUrl + dto.partnerDto().profileImageStoreName()
            );
        }
    }

    public record ReadChatRoomAuctionInfoResponse(Long id, String title, String image, int price) {

        private static ReadChatRoomAuctionInfoResponse of(
                final ReadSingleChatRoomDto dto,
                final String imageRelativeUrl
        ) {
            return new ReadChatRoomAuctionInfoResponse(
                    dto.auctionDto().id(),
                    dto.auctionDto().title(),
                    imageRelativeUrl + dto.auctionDto().thumbnailImageStoreName(),
                    dto.auctionDto().lastBidPrice()
            );
        }
    }
}
