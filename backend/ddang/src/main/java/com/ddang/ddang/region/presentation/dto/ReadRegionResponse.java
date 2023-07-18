package com.ddang.ddang.region.presentation.dto;

import com.ddang.ddang.region.application.dto.ReadRegionDto;

public record ReadRegionResponse(Long id, String name) {

    public static ReadRegionResponse from(final ReadRegionDto dto) {
        return new ReadRegionResponse(dto.id(), dto.name());
    }
}
