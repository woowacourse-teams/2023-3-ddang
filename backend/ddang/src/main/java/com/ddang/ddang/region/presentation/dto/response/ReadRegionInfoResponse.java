package com.ddang.ddang.region.presentation.dto.response;

import com.ddang.ddang.region.application.dto.ReadRegionDto;

public record ReadRegionInfoResponse(Long id, String name) {

    public static ReadRegionInfoResponse from(final ReadRegionDto dto) {
        return new ReadRegionInfoResponse(dto.id(), dto.name());
    }
}
