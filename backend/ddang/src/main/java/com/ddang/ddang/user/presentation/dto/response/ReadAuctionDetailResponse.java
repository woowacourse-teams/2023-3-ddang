package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.presentation.util.ReliabilityProcessor;

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
        final String profileImageUrl = ImageUrlCalculator.calculateBy(
                ImageRelativeUrl.USER,
                dto.auctionDto().sellerProfileImageStoreName()
        );
        final Float reliability = ReliabilityProcessor.process(dto.auctionDto().sellerReliability());

        final SellerResponse sellerResponse = new SellerResponse(
                dto.auctionDto().sellerId(),
                profileImageUrl,
                dto.auctionDto().sellerName(),
                reliability
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
