package com.ddang.ddang.authentication.infrastructure.oauth2.kakao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.matchesPattern;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withUnauthorizedRequest;

import com.ddang.ddang.authentication.configuration.KakaoProvidersConfigurationProperties;
import com.ddang.ddang.authentication.configuration.Oauth2PropertiesConfiguration;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.infrastructure.oauth2.kakao.fixture.KakaoUserInformationProviderFixture;
import com.ddang.ddang.configuration.RestTemplateConfiguration;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@RestClientTest({KakaoUserInformationProvider.class})
@Import({RestTemplateConfiguration.class, Oauth2PropertiesConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class KakaoUserInformationProviderTest extends KakaoUserInformationProviderFixture {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    KakaoProvidersConfigurationProperties kakaoProperties;

    @Autowired
    KakaoUserInformationProvider provider;

    MockRestServiceServer kakaoServer;

    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        kakaoServer = MockRestServiceServer.createServer(restTemplate);
    }

    @Test
    void 지원하는_소셜_로그인_타입을_반환한다() {
        final Oauth2Type actual = provider.supportsOauth2Type();

        assertThat(actual).isEqualTo(Oauth2Type.KAKAO);
    }

    @Test
    void 유효한_카카오_토큰을_전달한_경우_회원_정보를_조회한다() throws Exception {
        // given
        kakaoServer.expect(requestTo(matchesPattern(kakaoProperties.userInfoUri())))
                   .andRespond(
                         withSuccess(
                                 objectMapper.writeValueAsString(회원_정보),
                                 MediaType.APPLICATION_JSON
                         )
                 );

        // when
        final UserInformationDto actual = provider.findUserInformation(유효한_토큰);

        // then
        assertThat(actual).isEqualTo(회원_정보);
    }

    @Test
    void 유효하지_않은_카카오_토큰을_전달한_경우_예외가_발생한다() {
        // given
        kakaoServer.expect(requestTo(matchesPattern(kakaoProperties.userInfoUri())))
                   .andRespond(withUnauthorizedRequest());

        // when & then
        assertThatThrownBy(() -> provider.findUserInformation(유효하지_않은_토큰))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("401 Unauthorized");
    }

    @Test
    void 유효한_카카오_토큰을_전달한_경우_카카오_연결을_끊는다() throws Exception {
        // given
        kakaoServer.expect(requestTo(matchesPattern(kakaoProperties.userUnlinkUri())))
                   .andRespond(
                         withSuccess(
                                 objectMapper.writeValueAsString(회원_정보),
                                 MediaType.APPLICATION_JSON
                         )
                 );

        // when
        final UserInformationDto actual = provider.unlinkUserBy(카카오_회원_식별자);

        // then
        assertThat(actual).isEqualTo(회원_정보);
    }
}
