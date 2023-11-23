package com.ddang.ddang.region.application.dto.response;

import com.ddang.ddang.region.domain.Region;

public record ReadRegionDto(Long id, String name) {

    public static ReadRegionDto from(final Region region) {
        return new ReadRegionDto(region.getId(), region.getName());
    }
}
