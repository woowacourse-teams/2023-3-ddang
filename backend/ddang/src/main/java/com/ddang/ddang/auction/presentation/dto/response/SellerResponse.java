package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        Double reliability
) {

    public static SellerResponse from(final ReadAuctionDto auctionDto) {
        return new SellerResponse(
                auctionDto.sellerId(),
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, auctionDto.sellerProfileId()),
                auctionDto.sellerName(),
                auctionDto.sellerReliability()
        );
    }
}
