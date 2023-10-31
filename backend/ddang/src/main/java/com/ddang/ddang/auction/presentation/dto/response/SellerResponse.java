package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.image.presentation.util.ImageUrlCalculator;
import com.ddang.ddang.user.presentation.util.NameProcessor;
import com.ddang.ddang.user.presentation.util.ReliabilityProcessor;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        Float reliability
) {

    public static SellerResponse from(final ReadAuctionDto auctionDto) {
        return new SellerResponse(
                auctionDto.sellerId(),
                ImageUrlCalculator.calculateBy(ImageRelativeUrl.USER, auctionDto.sellerProfileId()),
                NameProcessor.process(auctionDto.isSellerDeleted(), auctionDto.sellerName()),
                ReliabilityProcessor.process(auctionDto.sellerReliability())
        );
    }
}
