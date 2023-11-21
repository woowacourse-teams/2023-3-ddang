package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

public record ReadAuctionDetailResponse(
        AuctionDetailResponse auction,
        SellerResponse seller,
        ChatRoomInAuctionResponse chat,
        boolean isOwner,
        boolean isLastBidder
) {

    public static ReadAuctionDetailResponse of(
            final ReadAuctionDto auctionDto,
            final AuthenticationUserInfo userInfo,
            final ReadChatRoomDto chatRoomDto,
            final ImageRelativeUrl userImageRelativeUrl,
            final ImageRelativeUrl auctionImageRelativeUrl
    ) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.of(auctionDto, auctionImageRelativeUrl);
        final SellerResponse sellerResponse = SellerResponse.of(auctionDto, userImageRelativeUrl);
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(chatRoomDto);

        return new ReadAuctionDetailResponse(
                auctionDetailResponse,
                sellerResponse,
                chatRoomResponse,
                isOwner(auctionDto, userInfo),
                isLastBidder(auctionDto, userInfo)
        );
    }

    private static boolean isOwner(final ReadAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return auctionDto.sellerId().equals(userInfo.userId());
    }

    private static boolean isLastBidder(final ReadAuctionDto auctionDto, final AuthenticationUserInfo userInfo) {
        return userInfo.userId().equals(auctionDto.lastBidderId());
    }
}
