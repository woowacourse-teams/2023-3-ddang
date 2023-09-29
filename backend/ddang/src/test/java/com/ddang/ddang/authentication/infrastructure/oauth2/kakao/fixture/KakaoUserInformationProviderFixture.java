package com.ddang.ddang.authentication.infrastructure.oauth2.kakao.fixture;

import com.ddang.ddang.authentication.configuration.KakaoProvidersConfigurationProperties;
import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;

@SuppressWarnings("NonAsciiCharacters")
public class KakaoUserInformationProviderFixture {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    protected KakaoProvidersConfigurationProperties 카카오_소셜_로그인_설정;

    protected MockRestServiceServer 카카오_인증_서버;
    protected String 카카오_회원_식별자 = "12345";
    protected UserInformationDto 회원_정보 = new UserInformationDto(12345L);
    protected String 유효한_토큰 = "Bearer accessToken";
    protected String 유효하지_않은_토큰 = "Bearer invalidAccessToken";

    @BeforeEach
    void setUp() {
        카카오_인증_서버 = MockRestServiceServer.createServer(restTemplate);
    }
}
