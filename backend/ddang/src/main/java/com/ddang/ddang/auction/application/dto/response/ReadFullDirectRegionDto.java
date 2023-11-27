package com.ddang.ddang.auction.application.dto.response;

import com.ddang.ddang.region.domain.AuctionRegion;
import com.ddang.ddang.region.domain.Region;

public record ReadFullDirectRegionDto(
        DirectRegionInfoDto firstRegionDto,
        DirectRegionInfoDto secondRegionDto,
        DirectRegionInfoDto thirdRegionDto
) {

    public static ReadFullDirectRegionDto from(final AuctionRegion auctionRegion) {
        final Region thirdRegion = auctionRegion.getThirdRegion();

        return new ReadFullDirectRegionDto(
                DirectRegionInfoDto.from(thirdRegion.getSecondRegion().getFirstRegion()),
                DirectRegionInfoDto.from(thirdRegion.getSecondRegion()),
                DirectRegionInfoDto.from(thirdRegion)
        );
    }

    public record DirectRegionInfoDto(Long regionId, String regionName) {

        public static DirectRegionInfoDto from(final Region region) {
            return new DirectRegionInfoDto(region.getId(), region.getName());
        }
    }
}
