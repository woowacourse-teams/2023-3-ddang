package com.ddang.ddang.region.infrastructure.api.dto;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiAccessTokenResponse {

    private String id;
    private Map<String, String> result;
    private String errMsg;
    private String errCd;
    private String trId;

    public ApiAccessTokenResponse(final Map<String, String> result) {
        this.result = result;
    }
}
