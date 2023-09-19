package com.ddang.ddang.authentication.presentation;

import com.ddang.ddang.authentication.application.AuthenticationService;
import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.application.dto.TokenDto;
import com.ddang.ddang.authentication.application.exception.InvalidWithdrawalException;
import com.ddang.ddang.authentication.configuration.Oauth2TypeConverter;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.authentication.infrastructure.oauth2.Oauth2Type;
import com.ddang.ddang.authentication.presentation.dto.request.LoginTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.LogoutRequest;
import com.ddang.ddang.authentication.presentation.dto.request.RefreshTokenRequest;
import com.ddang.ddang.authentication.presentation.dto.request.WithdrawalRequest;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuthenticationController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuthenticationControllerTest {

    @MockBean
    AuthenticationService authenticationService;

    @MockBean
    BlackListTokenService blackListTokenService;

    @Autowired
    AuthenticationController authenticationController;

    @MockBean
    AuthenticationUserService authenticationUserService;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired RestDocumentationContextProvider provider) {
        final FormattingConversionService formattingConversionService = new FormattingConversionService();
        formattingConversionService.addConverter(new Oauth2TypeConverter());

        mockMvc = MockMvcBuilders.standaloneSetup(authenticationController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setConversionService(formattingConversionService)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 소셜_로그인을_지원하는_타입과_소셜_로그인_토큰을_전달하면_accessToken과_refreshToken을_반환한다() throws Exception {
        // given
        final TokenDto tokenDto = new TokenDto("accessToken", "refreshToken");
        final LoginTokenRequest request = new LoginTokenRequest("kakaoAccessToken", "deviceToken");

        given(authenticationService.login(eq(Oauth2Type.KAKAO), anyString(), anyString())).willReturn(tokenDto);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/login/{oauth2Type}", "kakao")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.accessToken").exists(),
                       jsonPath("$.refreshToken").exists()
               )
               .andDo(
                       restDocs.document(
                               pathParameters(
                                       parameterWithName("oauth2Type").description("소셜 로그인을 할 서비스 선택(kakao로 고정)")
                               ),
                               requestFields(
                                       fieldWithPath("accessToken").description("소셜 로그인 AccessToken"),
                                       fieldWithPath("deviceToken").description("기기 디바이스 토큰")
                               ),
                               responseFields(
                                       fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Access Token"),
                                       fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("Refresh Token")
                               )
                       )
               );
    }

    @Test
    void 소셜_로그인을_진행하지_않는_타입을_전달하면_400이_발생한다() throws Exception {
        // given
        final LoginTokenRequest request = new LoginTokenRequest("kakaoAccessToken", "deviceToken");

        given(authenticationService.login(eq(Oauth2Type.KAKAO), anyString(), anyString()))
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
        final LoginTokenRequest request = new LoginTokenRequest(invalidKakaoAccessToken, "deviceToken");

        given(authenticationService.login(eq(Oauth2Type.KAKAO), anyString(), anyString()))
                .willThrow(new InvalidTokenException("401 Unauthorized", new RuntimeException()));

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
               )
               .andDo(
                       restDocs.document(
                               requestFields(
                                       fieldWithPath("refreshToken").description("refreshToken")
                               ),
                               responseFields(
                                       fieldWithPath("accessToken").type(JsonFieldType.STRING).description("재발급한 Access Token"),
                                       fieldWithPath("refreshToken").type(JsonFieldType.STRING).description("기존 Refresh Token")
                               )
                       )
               );
    }

    @Test
    void 유효하지_않은_refreshToken을_전달하면_401을_반환한다() throws Exception {
        // given
        final String invalidRefreshToken = "invalidRefreshToken";
        final RefreshTokenRequest request = new RefreshTokenRequest(invalidRefreshToken);

        willThrow(new InvalidTokenException("유효한 토큰이 아닙니다.")).given(authenticationService)
                                                             .refreshToken(anyString());

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

    @Test
    void 유효한_accessToken을_검증하면_참을_반환한다() throws Exception {
        // given
        given(authenticationService.validateToken(anyString())).willReturn(true);

        // when & then
        mockMvc.perform(get("/oauth2/validate-token")
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.validated").value(true)
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               responseFields(
                                       fieldWithPath("validated").type(JsonFieldType.BOOLEAN).description("Access Token이 유효한지 여부")
                               )
                       )
               );
    }

    @Test
    void 만료된_accessToken을_검증하면_거짓을_반환한다() throws Exception {
        // given
        given(authenticationService.validateToken(anyString())).willReturn(false);

        // when & then
        mockMvc.perform(get("/oauth2/validate-token")
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer invalidAccessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.validated").value(false)
               );
    }

    @Test
    void accessToken과_refreshToken을_전달하면_로그아웃한다() throws Exception {
        // given
        final LogoutRequest request = new LogoutRequest("Bearer refreshToken");

        willDoNothing().given(blackListTokenService).registerBlackListToken(anyString(), anyString());

        // when & then
        mockMvc.perform(post("/oauth2/logout")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(request))
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isNoContent()
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               requestFields(
                                       fieldWithPath("refreshToken").description("refreshToken")
                               )
                       )
               );
    }

    @Test
    void ouath2Type과_accessToken과_refreshToken을_전달하면_탈퇴한다() throws Exception {
        // given
        final WithdrawalRequest request = new WithdrawalRequest("Bearer refreshToken");

        willDoNothing().given(authenticationService).withdrawal(any(), anyString(), anyString());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/withdrawal/{oauth2Type}", "kakao")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(request))
                                                        .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isNoContent()
               )
               .andDo(
                       restDocs.document(
                               pathParameters(
                                       parameterWithName("oauth2Type").description("소셜 로그인을 할 서비스 선택(kakao로 고정)")
                               ),
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               requestFields(
                                       fieldWithPath("refreshToken").description("refreshToken")
                               )
                       )
               );
    }

    @Test
    void ouath2Type과_accessToken과_refreshToken을_전달시_이미_탈퇴_혹은_존재하지_않아_권한이_없는_회원인_경우_403을_반환한다() throws Exception {
        // given
        final WithdrawalRequest request = new WithdrawalRequest("Bearer refreshToken");

        willThrow(new InvalidWithdrawalException("탈퇴에 대한 권한 없습니다.")).given(authenticationService)
                                                                    .withdrawal(any(), anyString(), anyString());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/withdrawal/{oauth2Type}", "kakao")
                                                        .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message").value("탈퇴에 대한 권한 없습니다.")
               );
    }
}
