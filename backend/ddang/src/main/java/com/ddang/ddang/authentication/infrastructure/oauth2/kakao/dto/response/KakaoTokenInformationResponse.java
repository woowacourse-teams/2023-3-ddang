package com.ddang.ddang.authentication.infrastructure.oauth2.kakao.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KakaoTokenInformationResponse(
        Long id,

        @JsonProperty("expires_in")
        Integer expiresIn,

        @JsonProperty("app_id")
        Integer appId
) {
}
