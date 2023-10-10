package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;

public record ReadRegionsDto(
        ReadRegionDto firstRegionDto,
        ReadRegionDto secondRegionDto,
        ReadRegionDto thirdRegionDto
) {

    public static ReadRegionsDto from(final AuctionRegion auctionRegion) {
        final Region thirdRegion = auctionRegion.getThirdRegion();

        return new ReadRegionsDto(
                ReadRegionDto.from(thirdRegion.getSecondRegion().getFirstRegion()),
                ReadRegionDto.from(thirdRegion.getSecondRegion()),
                ReadRegionDto.from(thirdRegion)
        );
    }
}
