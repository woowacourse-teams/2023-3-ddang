package com.ddang.ddang.chat.presentation.dto.response;

import com.ddang.ddang.chat.application.dto.response.ReadSingleChatRoomDto;

public record ReadSingleChatRoomResponse(
        Long id,
        PartnerInfoResponse partner,
        DetailAuctionInfoResponse auction,
        boolean isChatAvailable
) {

    public static ReadSingleChatRoomResponse of(
            final ReadSingleChatRoomDto chatRoomDto,
            final String profileImageRelativeUrl,
            final String auctionImageRelativeUrl
    ) {
        final PartnerInfoResponse chatPartner = PartnerInfoResponse.of(chatRoomDto, profileImageRelativeUrl);
        final DetailAuctionInfoResponse auction = DetailAuctionInfoResponse.of(chatRoomDto, auctionImageRelativeUrl);

        return new ReadSingleChatRoomResponse(chatRoomDto.id(), chatPartner, auction, chatRoomDto.isChatAvailable());
    }

    public record PartnerInfoResponse(Long id, String name, String profileImage) {

        public static PartnerInfoResponse of(final ReadSingleChatRoomDto dto, final String imageRelativeUrl) {
            return new PartnerInfoResponse(
                    dto.partnerDto().id(),
                    dto.partnerDto().name(),
                    imageRelativeUrl + dto.partnerDto().profileImageStoreName()
            );
        }
    }

    public record DetailAuctionInfoResponse(Long id, String title, String image, int price) {

        public static DetailAuctionInfoResponse of(
                final ReadSingleChatRoomDto dto,
                final String imageRelativeUrl
        ) {
            return new DetailAuctionInfoResponse(
                    dto.auctionDto().id(),
                    dto.auctionDto().title(),
                    imageRelativeUrl + dto.auctionDto().thumbnailImageStoreName(),
                    dto.auctionDto().lastBidPrice()
            );
        }
    }
}
