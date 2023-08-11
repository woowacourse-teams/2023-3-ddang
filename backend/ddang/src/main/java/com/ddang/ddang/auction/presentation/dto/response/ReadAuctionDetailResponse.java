package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;

public record ReadAuctionDetailResponse(AuctionDetailResponse auction, SellerResponse seller,
                                        ChatRoomInAuctionResponse chat) {

    public static ReadAuctionDetailResponse of(final ReadAuctionWithChatRoomIdDto dto, final String baseUrl) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.of(dto.auctionDto(), baseUrl);
        final SellerResponse sellerResponse = new SellerResponse(
                dto.auctionDto().sellerId(),
                dto.auctionDto().sellerProfile(),
                dto.auctionDto().sellerName(),
                dto.auctionDto().sellerReliability()
        );
        final ChatRoomInAuctionResponse chatRoomResponse = ChatRoomInAuctionResponse.from(dto.chatRoomDto());

        return new ReadAuctionDetailResponse(auctionDetailResponse, sellerResponse, chatRoomResponse);
    }
}
