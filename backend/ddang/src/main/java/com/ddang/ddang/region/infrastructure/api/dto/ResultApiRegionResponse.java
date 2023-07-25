package com.ddang.ddang.region.infrastructure.api.dto;

import com.ddang.ddang.region.domain.Region;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ResultApiRegionResponse {

    @JsonProperty("y_coor")
    private String latitude;

    @JsonProperty("full_addr")
    private String fullAddress;

    @JsonProperty("x_coor")
    private String longitude;

    @JsonProperty("addr_name")
    private String regionName;

    @JsonProperty("cd")
    private String regionCode;

    public ResultApiRegionResponse(final String regionName, final String regionCode) {
        this.regionName = regionName;
        this.regionCode = regionCode;
    }

    public Region toEntity() {
        return new Region(regionName);
    }
}
