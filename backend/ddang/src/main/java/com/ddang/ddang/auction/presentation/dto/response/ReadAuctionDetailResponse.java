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
            final AuthenticationUserInfo userInfo
    ) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.from(dto.auctionDto());
        final SellerResponse sellerResponse = SellerResponse.from(dto.auctionDto());
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(dto.chatRoomDto());

        return new ReadAuctionDetailResponse(
                auctionDetailResponse,
                sellerResponse,
                chatRoomResponse,
                isOwner(dto, userInfo)
        );
    }

    private static boolean isOwner(final ReadAuctionWithChatRoomIdDto dto, final AuthenticationUserInfo userInfo) {
        return dto.auctionDto().sellerId().equals(userInfo.userId());
    }
}
