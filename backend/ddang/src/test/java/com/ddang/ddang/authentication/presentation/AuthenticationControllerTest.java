package com.ddang.ddang.authentication.presentation;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.authentication.application.AuthenticationService;
import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.application.exception.InvalidTokenException;
import com.ddang.ddang.authentication.configuration.Oauth2TypeConverter;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.infrastructure.oauth2.exception.InvalidSocialOauth2TokenException;
import com.ddang.ddang.authentication.presentation.dto.request.AccessTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.RefreshTokenRequest;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(controllers = {AuthenticationController.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationControllerTest {

    @MockBean
    AuthenticationService authenticationService;

    @Autowired
    AuthenticationController authenticationController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        final FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addConverter(new Oauth2TypeConverter());

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setConversionService(formattingConversionService)
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 소셜_로그인을_지원하는_타입과_소셜_로그인_토큰을_전달하면_accessToken과_refreshToken을_반환한다() throws Exception {
        // given
        final TokenDto tokenDto = new TokenDto("accessToken", "refreshToken");
        final AccessTokenRequest request = new AccessTokenRequest("kakaoAccessToken");

        given(authenticationService.login(eq(Oauth2Type.KAKAO), anyString())).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/oauth2/login/{oauth2Type}", "kakao")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.accessToken").exists(),
                       jsonPath("$.refreshToken").exists()
               );
    }

    @Test
    void 소셜_로그인을_진행하지_않는_타입을_전달하면_400이_발생한다() throws Exception {
        // given
        final AccessTokenRequest request = new AccessTokenRequest("kakaoAccessToken");

        given(authenticationService.login(eq(Oauth2Type.KAKAO), anyString()))
                .willThrow(new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다."));

        // when & then
        mockMvc.perform(post("/oauth2/login/{oauth2Type}", "kakao")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 유효하지_않은_소셜_로그인_토큰을_전달하면_401이_발생한다() throws Exception {
        // given
        final String invalidKakaoAccessToken = "invalidKakaoAccessToken";
        final AccessTokenRequest request = new AccessTokenRequest(invalidKakaoAccessToken);

        given(authenticationService.login(eq(Oauth2Type.KAKAO), anyString()))
                .willThrow(new InvalidSocialOauth2TokenException("401 Unauthorized", new RuntimeException()));

        // when & then
        mockMvc.perform(post("/oauth2/login/{oauth2Type}", "kakao")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 유효한_refreshToken을_전달하면_새로운_accessToken과_refreshToken을_반환한다() throws Exception {
        // given
        final TokenDto tokenDto = new TokenDto("accessToken", "refreshToken");
        final RefreshTokenRequest request = new RefreshTokenRequest("refreshToken");

        given(authenticationService.refreshToken(anyString())).willReturn(tokenDto);

        // when & then
        mockMvc.perform(post("/oauth2/refresh-token")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.accessToken").exists(),
                       jsonPath("$.refreshToken").exists()
               );
    }

    @Test
    void 유효하지_않은_refreshToken을_전달하면_401을_반환한다() throws Exception {
        // given
        final String invalidRefreshToken = "invalidRefreshToken";
        final RefreshTokenRequest request = new RefreshTokenRequest(invalidRefreshToken);

        //given(authenticationService.refreshToken(anyString())).willThrow(new InvalidTokenException("유효한 토큰이 아닙니다."));
        willThrow(new InvalidTokenException("유효한 토큰이 아닙니다.")).given(authenticationService).refreshToken(anyString());

        // when & then
        mockMvc.perform(post("/oauth2/refresh-token")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.message").exists()
               );
    }
}
