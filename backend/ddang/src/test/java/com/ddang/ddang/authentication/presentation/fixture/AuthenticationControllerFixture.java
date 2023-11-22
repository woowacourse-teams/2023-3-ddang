package com.ddang.ddang.authentication.presentation.fixture;

import com.ddang.ddang.authentication.application.dto.response.LoginInfoDto;
import com.ddang.ddang.authentication.application.dto.response.SocialUserInfoDto;
import com.ddang.ddang.authentication.application.dto.response.TokenDto;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.presentation.dto.request.LoginTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.LogoutRequest;
import com.ddang.ddang.authentication.presentation.dto.request.RefreshTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.WithdrawalRequest;
import com.ddang.ddang.configuration.CommonControllerSliceTest;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationControllerFixture extends CommonControllerSliceTest {

    protected Oauth2Type 지원하는_소셜_로그인_타입 = Oauth2Type.KAKAO;
    protected Oauth2Type 지원하지_않는_소셜_로그인_타입 = Oauth2Type.KAKAO;
    protected String 소셜_로그인_타입 = "kakao";
    protected String 유효한_액세스_토큰_내용 = "Bearer accessToken";
    protected String 만료된_액세스_토큰_내용 = "Bearer accessToken";
    protected TokenDto 발급된_토큰 = new TokenDto(유효한_액세스_토큰_내용, "Bearer refreshToken");
    protected LoginInfoDto 로그인한_사용자_정보 = new LoginInfoDto(발급된_토큰.accessToken(), 발급된_토큰.refreshToken(), false);
    protected LoginTokenRequest 유효한_로그인_요청 = new LoginTokenRequest("kakaoAccessToken", "deviceToken");
    protected LoginTokenRequest 유효하지_않은_로그인_요청 = new LoginTokenRequest("kakaoAccessToken", "deviceToken");
    protected RefreshTokenRequest 유효한_토큰_재발급_요청 = new RefreshTokenRequest("Bearer refreshToken");
    protected RefreshTokenRequest 유효하지_않은_토큰_재발급_요청 = new RefreshTokenRequest("Basic refreshToken");
    protected LogoutRequest 유효한_로그아웃_요청 = new LogoutRequest("Bearer refreshToken");
    protected WithdrawalRequest 유효한_회원탈퇴_요청 = new WithdrawalRequest("Bearer refreshToken");
    protected WithdrawalRequest 유효하지_않은_회원탈퇴_요청 = new WithdrawalRequest("Bearer refreshToken");
    protected SocialUserInfoDto 회원_소셜_정보 = new SocialUserInfoDto("12345");
}
