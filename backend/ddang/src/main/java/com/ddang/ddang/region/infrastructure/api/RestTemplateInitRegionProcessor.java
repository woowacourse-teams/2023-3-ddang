package com.ddang.ddang.region.infrastructure.api;

import com.ddang.ddang.region.domain.InitializationRegionProcessor;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.api.dto.ApiAccessTokenResponse;
import com.ddang.ddang.region.infrastructure.api.dto.ResultApiRegionResponse;
import com.ddang.ddang.region.infrastructure.api.dto.TotalApiRegionResponse;
import com.ddang.ddang.region.infrastructure.api.exception.RegionApiException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class RestTemplateInitRegionProcessor implements InitializationRegionProcessor {

    private static final String REGION_API_DOMAIN = "https://sgisapi.kostat.go.kr";
    private static final String REGION_CODE_NAME = "cd";
    private static final String ACCESS_TOKEN_NAME = "accessToken";
    private static final String SERVICE_KEY_NAME = "consumer_key";
    private static final String SECRET_KEY_NAME = "consumer_secret";
    private static final String AUTHENTICATION_URL = "/OpenAPI3/auth/authentication.json";
    private static final String REGION_URL_NAME = "/OpenAPI3/addr/stage.json";

    @Value("${open.api.region.service}")
    private String serviceSecret;

    @Value("${open.api.region.key}")
    private String keySecret;

    private final RestTemplate restTemplate;

    @Override
    public List<Region> requestRegions() {
        final String accessToken = requestApiAccessToken();
        final List<ResultApiRegionResponse> firstRegionsResponses = requestApiRegionResponse(
                null,
                accessToken
        );

        final List<Region> totalRegions = new ArrayList<>();

        for (final ResultApiRegionResponse firstRegionResponse : firstRegionsResponses) {
            final Region firstRegion = firstRegionResponse.toEntity();
            final List<ResultApiRegionResponse> secondRegionsResponses = requestApiRegionResponse(
                    firstRegionResponse.getRegionCode(),
                    accessToken
            );

            for (final ResultApiRegionResponse secondRegionsResponse : secondRegionsResponses) {
                final Region secondRegion = secondRegionsResponse.toEntity();
                final List<ResultApiRegionResponse> thirdRegionsResponses = requestApiRegionResponse(
                        secondRegionsResponse.getRegionCode(),
                        accessToken
                );

                firstRegion.addSecondRegion(secondRegion);

                for (final ResultApiRegionResponse thirdRegionsResponse : thirdRegionsResponses) {
                    final Region thirdRegion = thirdRegionsResponse.toEntity();
                    secondRegion.addThirdRegion(thirdRegion);
                }
            }

            totalRegions.add(firstRegion);
        }

        return totalRegions;
    }

    private String requestApiAccessToken() {
        final URI authenticationUri = UriComponentsBuilder.fromUriString(REGION_API_DOMAIN)
                                                    .path(AUTHENTICATION_URL)
                                                    .queryParam(SERVICE_KEY_NAME, serviceSecret)
                                                    .queryParam(SECRET_KEY_NAME, keySecret)
                                                    .encode()
                                                    .build()
                                                    .toUri();

        final ApiAccessTokenResponse accessTokenResponse = restTemplate.getForObject(
                authenticationUri,
                ApiAccessTokenResponse.class
        );
        try {
            return accessTokenResponse.getResult()
                                      .get(ACCESS_TOKEN_NAME);
        } catch (final NullPointerException ex) {
            final String exceptionFormatter =  "API 요청 도중 문제가 발생했습니다. 코드 번호 : %s, 메세지 : %s";
            throw new RegionApiException(
                    String.format(
                            exceptionFormatter,
                            accessTokenResponse.getErrCd(),
                            accessTokenResponse.getErrMsg()
                    ), ex
            );
        }
    }

    private List<ResultApiRegionResponse> requestApiRegionResponse(final String regionCode, final String accessToken) {
        final UriComponents regionUri = createRegionUriBuilder(accessToken, regionCode);
        final TotalApiRegionResponse regionResponse = restTemplate.getForObject(
                regionUri.toUri(),
                TotalApiRegionResponse.class
        );

        try {
            return regionResponse.getResult();
        } catch (final NullPointerException ex) {
            throw new RegionApiException("API 요청 도중 문제가 발생했습니다.", ex);
        }
    }

    private UriComponents createRegionUriBuilder(final String accessToken, final String regionCode) {
        final UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(REGION_API_DOMAIN)
                                                                 .path(REGION_URL_NAME)
                                                                 .queryParam(ACCESS_TOKEN_NAME, accessToken);

        if (regionCode != null && !regionCode.isEmpty()) {
            builder.queryParam(REGION_CODE_NAME, regionCode);
        }

        return builder.encode()
                      .build();
    }
}
