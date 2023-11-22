package com.ddang.ddang.auction.application.dto.response;

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

    private record ReadRegionDto(Long regionId, String regionName) {

        public static ReadRegionDto from(final Region region) {
            return new ReadRegionDto(region.getId(), region.getName());
        }
    }

    public String firstRegionName() {
        return firstRegionDto.regionName;
    }

    public String secondRegionName() {
        return secondRegionDto.regionName;
    }

    public String thirdRegionName() {
        return thirdRegionDto.regionName;
    }
}
