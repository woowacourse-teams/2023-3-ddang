package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.region.domain.Region;

public record ReadRegionDto(Long regionId, String regionName) {

    public static ReadRegionDto from(final Region region) {
        return new ReadRegionDto(region.getId(), region.getName());
    }
}
