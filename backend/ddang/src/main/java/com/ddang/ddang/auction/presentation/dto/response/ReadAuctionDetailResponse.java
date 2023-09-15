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
            final String baseUrl,
            final AuthenticationUserInfo userInfo,
            final ReadChatRoomDto chatRoomDto
    ) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.of(auctionDto, baseUrl);
        final SellerResponse sellerResponse = new SellerResponse(
                auctionDto.sellerId(),
                auctionDto.sellerProfile(),
                auctionDto.sellerName(),
                auctionDto.sellerReliability()
        );
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(chatRoomDto);

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
