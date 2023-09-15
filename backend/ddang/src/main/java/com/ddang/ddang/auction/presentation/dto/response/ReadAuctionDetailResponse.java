package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;

public record ReadAuctionDetailResponse(
        AuctionDetailResponse auction,
        SellerResponse seller,
        ChatRoomInAuctionResponse chat,
        boolean isOwner
) {

    public static ReadAuctionDetailResponse of(
            final ReadAuctionDto auctionDto,
            final AuthenticationUserInfo userInfo,
            final ReadChatRoomDto chatRoomDto
    ) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.from(auctionDto());
        final SellerResponse sellerResponse = SellerResponse.from(auctionDto());
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(chatRoomDto());

        return new ReadAuctionDetailResponse(
                auctionDetailResponse,
                sellerResponse,
                chatRoomResponse,
                isOwner(auctionDto, userInfo)
        );
    }

    private static boolean isOwner(final ReadAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return auctionDto.sellerId().equals(userInfo.userId());
    }
}
