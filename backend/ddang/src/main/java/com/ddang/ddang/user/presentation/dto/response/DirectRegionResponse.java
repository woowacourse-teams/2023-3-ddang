package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.ReadRegionsDto;

public record DirectRegionResponse(String first, String second, String third) {

    public static DirectRegionResponse from(final ReadRegionsDto dto) {
        return new DirectRegionResponse(
                dto.firstRegionDto().regionName(),
                dto.secondRegionDto().regionName(),
                dto.thirdRegionDto().regionName()
        );
    }
}
