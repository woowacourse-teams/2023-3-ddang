package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;
import com.ddang.ddang.user.presentation.util.NameProcessor;
import com.ddang.ddang.user.presentation.util.ReliabilityProcessor;

public record SellerResponse(
        Long id,
        String image,
        String nickname,
        Float reliability
) {

    public static SellerResponse of(final ReadAuctionDto auctionDto, final ImageRelativeUrl imageRelativeUrl) {
        return new SellerResponse(
                auctionDto.sellerId(),
                imageRelativeUrl.calculateAbsoluteUrl() + auctionDto.sellerProfileStoreName(),
                NameProcessor.process(auctionDto.isSellerDeleted(), auctionDto.sellerName()),
                ReliabilityProcessor.process(auctionDto.sellerReliability())
        );
    }
}
