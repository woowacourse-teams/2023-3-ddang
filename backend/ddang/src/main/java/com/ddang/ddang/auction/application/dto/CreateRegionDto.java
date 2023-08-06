package com.ddang.ddang.auction.application.dto;

import com.ddang.ddang.auction.presentation.dto.request.CreateDirectRegionRequest;

public record CreateRegionDto(Long firstRegionId, Long secondRegionId, Long thirdRegionId) {

    public static CreateRegionDto from(final CreateDirectRegionRequest request) {
        return new CreateRegionDto(request.firstRegionId(), request.secondRegionId(), request.thirdRegionId());
    }
}
