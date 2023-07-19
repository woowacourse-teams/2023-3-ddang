package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;

public record ReadAuctionDetailResponse(AuctionDetailResponse auction, SellerResponse seller) {

    public static ReadAuctionDetailResponse from(final ReadAuctionDto dto) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.from(dto);
        final SellerResponse sellerResponse = new SellerResponse(
                1L,
                "https://img1.daumcdn.net/thumb/R1280x0/?fname=http://t1.daumcdn.net/brunch/service/user/7r5X/image/9djEiPBPMLu_IvCYyvRPwmZkM1g.jpg",
                "닉네임",
                5.0d
        );

        return new ReadAuctionDetailResponse(auctionDetailResponse, sellerResponse);
    }
}
