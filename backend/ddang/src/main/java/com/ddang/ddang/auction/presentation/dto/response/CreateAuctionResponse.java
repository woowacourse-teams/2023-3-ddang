package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.image.util.ImageBaseUrl;
import com.ddang.ddang.image.util.ImageUrlBuilder;

public record CreateAuctionResponse(
        Long id,
        String title,
        String image,
        int auctionPrice,
        String status,
        int auctioneerCount
) {

    public static CreateAuctionResponse from(final CreateInfoAuctionDto dto) {
        return new CreateAuctionResponse(
                dto.id(),
                dto.title(),
                getAuctionImageUrl(dto.auctionImageId()),
                dto.startPrice(),
                // TODO 2차 데모데이 이후 enum으로 처리
                "UNBIDDEN",
                0
        );
    }

    private static String getAuctionImageUrl(final Long id) {
        return ImageUrlBuilder.calculate(ImageBaseUrl.AUCTION, id);
    }
}
