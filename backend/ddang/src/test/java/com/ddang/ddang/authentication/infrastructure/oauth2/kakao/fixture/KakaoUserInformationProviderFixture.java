package com.ddang.ddang.authentication.infrastructure.oauth2.kakao.fixture;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import org.springframework.test.web.client.MockRestServiceServer;

@SuppressWarnings("NonAsciiCharacters")
public class KakaoUserInformationProviderFixture {

    protected MockRestServiceServer 카카오_인증_서버;
    protected String 카카오_회원_식별자 = "12345";
    protected UserInformationDto 회원_정보 = new UserInformationDto(12345L);
    protected String 유효한_토큰 = "Bearer accessToken";
    protected String 유효하지_않은_토큰 = "Bearer invalidAccessToken";
}
