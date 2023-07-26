package com.ddang.ddang.region.infrastructure.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.ddang.ddang.configuration.RestTemplateConfiguration;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.api.dto.ApiAccessTokenResponse;
import com.ddang.ddang.region.infrastructure.api.dto.ResultApiRegionResponse;
import com.ddang.ddang.region.infrastructure.api.dto.TotalApiRegionResponse;
import com.ddang.ddang.region.infrastructure.api.exception.RegionApiException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@RestClientTest({RestTemplateInitRegionProcessor.class})
@Import(RestTemplateConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RestTemplateInitRegionProcessorTest {

    private MockRestServiceServer mockRestServiceServer;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RestTemplateInitRegionProcessor regionProcessor;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void API_요청을_통해_대한민국_전국_지역을_조회한다() throws Exception {
        // given
        final String accessToken = "accessToken";
        final ApiAccessTokenResponse accessTokenResponse = new ApiAccessTokenResponse(
                Collections.singletonMap("accessToken", accessToken)
        );

        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json.*"
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(accessTokenResponse),
                        MediaType.APPLICATION_JSON
                ));

        final String firstRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken;
        final ResultApiRegionResponse firstResultApiRegionResponse = new ResultApiRegionResponse(
                "서울특별시",
                "11"
        );
        final TotalApiRegionResponse openApiResultApiRegionResponse = new TotalApiRegionResponse(
                List.of(firstResultApiRegionResponse)
        );

        mockRestServiceServer
                .expect(requestTo(firstRegionUri))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(openApiResultApiRegionResponse),
                        MediaType.APPLICATION_JSON
                ));

        final String secondRegionCode = "11";
        final String secondRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken +
                "&cd=" +
                secondRegionCode;
        final ResultApiRegionResponse secondResultApiRegionResponse = new ResultApiRegionResponse(
                "강남구",
                "11230"
        );
        final TotalApiRegionResponse secondOpenApiResultApiRegionResponse = new TotalApiRegionResponse(
                List.of(secondResultApiRegionResponse)
        );

        mockRestServiceServer
                .expect(requestTo(secondRegionUri))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(secondOpenApiResultApiRegionResponse),
                        MediaType.APPLICATION_JSON)
                );

        final String thirdRegionCode = "11230";
        final String thirdRegionUri = "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json?" +
                "accessToken=" +
                accessToken +
                "&cd=" +
                thirdRegionCode;
        final ResultApiRegionResponse thirdResultApiRegionResponse = new ResultApiRegionResponse(
                "개포1동",
                "11230680"
        );
        final TotalApiRegionResponse thirdOpenApiResultApiRegionResponse = new TotalApiRegionResponse(
                List.of(thirdResultApiRegionResponse)
        );

        mockRestServiceServer
                .expect(requestTo(thirdRegionUri))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(thirdOpenApiResultApiRegionResponse),
                        MediaType.APPLICATION_JSON
                ));

        // when
        final List<Region> actual = regionProcessor.requestRegions();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);

            final Region actualFirstRegion = actual.get(0);
            softAssertions.assertThat(actualFirstRegion.getName()).isEqualTo(firstResultApiRegionResponse.getRegionName());
            softAssertions.assertThat(actualFirstRegion.getSecondRegions()).hasSize(1);

            final Region actualSecondRegion = actualFirstRegion.getSecondRegions().get(0);
            softAssertions.assertThat(actualSecondRegion.getName()).isEqualTo(secondResultApiRegionResponse.getRegionName());
            softAssertions.assertThat(actualSecondRegion.getThirdRegions()).hasSize(1);

            final Region actualThirdRegion = actualSecondRegion.getThirdRegions().get(0);
            assertThat(actualThirdRegion.getName()).isEqualTo(thirdResultApiRegionResponse.getRegionName());
        });
    }

    @Test
    void service키나_secret키가_유효하지_않은_경우_예외가_발생한다() throws Exception {
        // given
        final ApiAccessTokenResponse invalidAccessTokenResponse = new ApiAccessTokenResponse(null);

        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json.*"
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(invalidAccessTokenResponse),
                        MediaType.APPLICATION_JSON
                ));

        // when & then
        assertThatThrownBy(() -> regionProcessor.requestRegions())
                .isInstanceOf(RegionApiException.class)
                .hasMessageContaining("API 요청 도중 문제가 발생했습니다.");
    }

    @Test
    void 단계별_주소_조회_API_요청에_실패하면_예외가_발생한다() throws Exception {
        // given
        final String accessToken = "accessToken";
        final ApiAccessTokenResponse accessTokenResponse = new ApiAccessTokenResponse(
                Collections.singletonMap("accessToken", accessToken)
        );

        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        "https://sgisapi.kostat.go.kr/OpenAPI3/auth/authentication.json.*"
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(accessTokenResponse),
                        MediaType.APPLICATION_JSON
                ));

        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        "https://sgisapi.kostat.go.kr/OpenAPI3/addr/stage.json.*"
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(null),
                        MediaType.APPLICATION_JSON
                ));

        // when & then
        assertThatThrownBy(() -> regionProcessor.requestRegions())
                .isInstanceOf(RegionApiException.class)
                .hasMessageContaining("API 요청 도중 문제가 발생했습니다.");
    }
}
