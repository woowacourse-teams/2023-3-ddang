package com.ddang.ddang.region.infrastructure.api.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TotalApiRegionResponse {

    private String id;
    private List<ResultApiRegionResponse> result;

    public TotalApiRegionResponse(final List<ResultApiRegionResponse> result) {
        this.result = result;
    }
}
