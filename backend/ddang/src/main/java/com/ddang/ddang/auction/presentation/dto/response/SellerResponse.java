package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.presentation.util.NameProcessor;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        Float reliability
) {

    public static SellerResponse from(final ReadAuctionDto auctionDto) {
        final Float floatReliability = Float.valueOf(String.valueOf(auctionDto.sellerReliability()));

        return new SellerResponse(
                auctionDto.sellerId(),
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, auctionDto.sellerProfileImageStoreName()),
                NameProcessor.process(auctionDto.isSellerDeleted(), auctionDto.sellerName()),
                floatReliability
        );
    }
}
