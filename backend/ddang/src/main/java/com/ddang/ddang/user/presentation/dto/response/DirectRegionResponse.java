package com.ddang.ddang.user.presentation.dto.response;

import com.ddang.ddang.auction.application.dto.response.ReadRegionsDto;

public record DirectRegionResponse(String first, String second, String third) {

    public static DirectRegionResponse from(final ReadRegionsDto dto) {
        return new DirectRegionResponse(dto.firstRegionName(), dto.secondRegionName(), dto.thirdRegionName());
    }
}
