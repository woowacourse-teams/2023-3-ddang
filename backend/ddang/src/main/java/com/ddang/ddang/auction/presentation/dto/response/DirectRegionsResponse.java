package com.ddang.ddang.auction.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadRegionsDto;

public record DirectRegionsResponse(String first, String second, String third) {

    public static DirectRegionsResponse from(final ReadRegionsDto dto) {
        return new DirectRegionsResponse(
                dto.firstRegionDto().regionName(),
                dto.secondRegionDto().regionName(),
                dto.thirdRegionDto().regionName()
        );
    }
}
