package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        double reliability
) {

    public static SellerResponse from(final ReadAuctionDto auctionDto) {
        return new SellerResponse(
                auctionDto.sellerId(),
                convertImageUrl(auctionDto.sellerProfileId()),
                auctionDto.sellerName(),
                auctionDto.sellerReliability()
        );
    }

    private static String convertImageUrl(final Long id) {
        return ImageUrlCalculator.calculate(ImageRelativeUrl.USER, id);
    }
}
