package com.ddang.ddang.region.infrastructure.api;

import com.ddang.ddang.configuration.RestTemplateConfiguration;
import com.ddang.ddang.region.domain.Region;
import com.ddang.ddang.region.infrastructure.api.exception.RegionApiException;
import com.ddang.ddang.region.infrastructure.api.fixture.RestTemplateInitRegionProcessorFixture;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

@RestClientTest({RestTemplateInitRegionProcessor.class})
@Import(RestTemplateConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RestTemplateInitRegionProcessorTest extends RestTemplateInitRegionProcessorFixture {

    MockRestServiceServer mockRestServiceServer;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RestTemplateInitRegionProcessor regionProcessor;

    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockRestServiceServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void API_요청을_통해_대한민국_전국_지역을_조회한다() throws Exception {
        // given
        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        액세스_토큰_요청_URI_패턴
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(유효한_액세스_토큰),
                        MediaType.APPLICATION_JSON
                ));

        mockRestServiceServer
                .expect(requestTo(첫번쪠_지역_목록_조회_URI))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(첫번째_지역_목록_조회_응답),
                        MediaType.APPLICATION_JSON
                ));

        mockRestServiceServer
                .expect(requestTo(서울특별시_하위_지역_목록_조회_URI))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(서울특별시_하위_지역_목록_조회_응답),
                        MediaType.APPLICATION_JSON)
                );

        mockRestServiceServer
                .expect(requestTo(서울특별시_강남구_하위_지역_목록_조회_URI))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(서울특별시_강남구의_하위_지역_목록_조회_응답),
                        MediaType.APPLICATION_JSON
                ));

        // when
        final List<Region> actual = regionProcessor.requestRegions();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(1);

            final Region actualFirstRegion = actual.get(0);
            softAssertions.assertThat(actualFirstRegion.getName()).isEqualTo(서울특별시.getRegionName());
            softAssertions.assertThat(actualFirstRegion.getSecondRegions()).hasSize(1);

            final Region actualSecondRegion = actualFirstRegion.getSecondRegions().get(0);
            softAssertions.assertThat(actualSecondRegion.getName()).isEqualTo(강남구.getRegionName());
            softAssertions.assertThat(actualSecondRegion.getThirdRegions()).hasSize(1);

            final Region actualThirdRegion = actualSecondRegion.getThirdRegions().get(0);
            assertThat(actualThirdRegion.getName()).isEqualTo(개포1동.getRegionName());
        });
    }

    @Test
    void service키나_secret키가_유효하지_않은_경우_예외가_발생한다() throws Exception {
        // given
        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        액세스_토큰_요청_URI_패턴
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(유효하지_않은_액세스_토큰),
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
        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        액세스_토큰_요청_URI_패턴
                )))
                .andRespond(withSuccess(
                        objectMapper.writeValueAsString(유효한_액세스_토큰),
                        MediaType.APPLICATION_JSON
                ));

        mockRestServiceServer
                .expect(requestTo(matchesPattern(
                        단계별_지역_목록_조회_URI_패턴
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
