package com.ddang.ddang.authentication.application.fixture;

import com.ddang.ddang.authentication.domain.dto.UserInformationDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;

@SuppressWarnings("NonAsciiCharacters")
public class SocialUserInformationServiceFixture {

    protected Oauth2Type 지원하는_소셜_로그인_타입 = Oauth2Type.KAKAO;
    protected Oauth2Type 지원하지_않는_소셜_로그인_타입 = Oauth2Type.KAKAO;
    protected String 유효한_소셜_로그인_토큰 = "Bearer accessToken";
    protected String 권한이_없는_소셜_로그인_토큰 = "Bearer no authorization";
    protected UserInformationDto 사용자_소셜_정보 = new UserInformationDto(1L);
}
