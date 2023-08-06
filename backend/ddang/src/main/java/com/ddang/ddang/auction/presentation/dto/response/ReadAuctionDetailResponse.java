package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;

public record ReadAuctionDetailResponse(AuctionDetailResponse auction, SellerResponse seller) {

    public static ReadAuctionDetailResponse of(final ReadAuctionDto dto, final String baseUrl) {
        final AuctionDetailResponse auctionDetailResponse = AuctionDetailResponse.of(dto, baseUrl);
        final SellerResponse sellerResponse = new SellerResponse(
                dto.sellerId(),
                dto.sellerProfile(),
                dto.sellerName(),
                dto.sellerReliability()
        );

        return new ReadAuctionDetailResponse(auctionDetailResponse, sellerResponse);
    }
}
