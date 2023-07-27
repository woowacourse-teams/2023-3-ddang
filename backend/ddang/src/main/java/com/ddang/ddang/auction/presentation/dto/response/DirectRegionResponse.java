package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadRegionDto;

public record DirectRegionResponse(Long regionId, String regionName) {

    public static DirectRegionResponse from(final ReadRegionDto dto) {
        return new DirectRegionResponse(dto.regionId(), dto.regionName());
    }
}
