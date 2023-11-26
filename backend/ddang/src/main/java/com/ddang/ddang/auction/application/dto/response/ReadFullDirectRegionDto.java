package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;

public record ReadFullDirectRegionDto(
        ReadDirectRegionDto firstRegionDto,
        ReadDirectRegionDto secondRegionDto,
        ReadDirectRegionDto thirdRegionDto
) {
    public static ReadFullDirectRegionDto from(final AuctionRegion auctionRegion) {
        final Region thirdRegion = auctionRegion.getThirdRegion();

        return new ReadFullDirectRegionDto(
                ReadDirectRegionDto.from(thirdRegion.getSecondRegion().getFirstRegion()),
                ReadDirectRegionDto.from(thirdRegion.getSecondRegion()),
                ReadDirectRegionDto.from(thirdRegion)
        );
    }

    public record ReadDirectRegionDto(Long regionId, String regionName) {

        private static ReadDirectRegionDto from(final Region region) {
            return new ReadDirectRegionDto(region.getId(), region.getName());
        }
    }
}
