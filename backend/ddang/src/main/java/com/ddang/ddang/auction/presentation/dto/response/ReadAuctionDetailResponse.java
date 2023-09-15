package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.user.presentation.util.NameProcessor;

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
        final boolean isDeleted = dto.auctionDto().isSellerDeleted();
        final String name = dto.auctionDto().sellerName();
        final SellerResponse sellerResponse = new SellerResponse(
                dto.auctionDto().sellerId(),
                dto.auctionDto().sellerProfile(),
                NameProcessor.process(isDeleted, name),
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
        return dto.auctionDto().sellerId().equals(userInfo.userId());
    }
}
