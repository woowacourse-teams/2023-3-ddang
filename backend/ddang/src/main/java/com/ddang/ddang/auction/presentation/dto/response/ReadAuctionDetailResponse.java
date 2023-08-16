package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;

public record ReadAuctionDetailResponse(
        AuctionDetailResponse auction,
        SellerResponse seller,
        ChatRoomInAuctionResponse chat,
        boolean isOwner
) {

    public static ReadAuctionDetailResponse of(
            final ReadAuctionWithChatRoomIdDto dto,
            final String baseUrl,
            final AuthenticationUserInfo userInfo
    ) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.of(dto.auctionDto(), baseUrl);
        final SellerResponse sellerResponse = new SellerResponse(
                dto.auctionDto().sellerId(),
                dto.auctionDto().sellerProfile(),
                dto.auctionDto().sellerName(),
                dto.auctionDto().sellerReliability()
        );
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(dto.chatRoomDto());

        return new ReadAuctionDetailResponse(
                auctionDetailResponse,
                sellerResponse,
                chatRoomResponse,
                isOwner(dto, userInfo)
        );
    }

    private static boolean isOwner(final ReadAuctionWithChatRoomIdDto dto, final AuthenticationUserInfo userInfo) {
        if (userInfo == null || userInfo.userId() == null) {
            return false;
        }
        return dto.auctionDto().sellerId().equals(userInfo.userId());
    }
}
