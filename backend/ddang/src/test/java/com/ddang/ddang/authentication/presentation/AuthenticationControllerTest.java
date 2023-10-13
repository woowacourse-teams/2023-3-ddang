package com.ddang.ddang.authentication.presentation;

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

import com.ddang.ddang.authentication.application.exception.InvalidWithdrawalException;
import com.ddang.ddang.authentication.configuration.Oauth2TypeConverter;
import com.ddang.ddang.authentication.domain.exception.InvalidTokenException;
import com.ddang.ddang.authentication.domain.exception.UnsupportedSocialLoginException;
import com.ddang.ddang.authentication.presentation.fixture.AuthenticationControllerFixture;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("NonAsciiCharacters")
class AuthenticationControllerTest extends AuthenticationControllerFixture {

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
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
        given(authenticationService.login(eq(지원하는_소셜_로그인_타입), anyString(), anyString())).willReturn(로그인한_사용자_정보);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/login/{oauth2Type}", 소셜_로그인_타입)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(유효한_로그인_요청))
                       )
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.accessToken").exists(),
                               jsonPath("$.refreshToken").exists(),
                               jsonPath("$.isSignUpUser").exists()
                       );

        login_문서화(resultActions);
    }

    @Test
    void 소셜_로그인을_진행하지_않는_타입을_전달하면_400이_발생한다() throws Exception {
        // given
        given(authenticationService.login(eq(지원하지_않는_소셜_로그인_타입), anyString(), anyString()))
                .willThrow(new UnsupportedSocialLoginException("지원하는 소셜 로그인 기능이 아닙니다."));

        // when & then
        mockMvc.perform(post("/oauth2/login/{oauth2Type}", 소셜_로그인_타입)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(유효하지_않은_로그인_요청))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 유효하지_않은_소셜_로그인_토큰을_전달하면_401이_발생한다() throws Exception {
        // given
        given(authenticationService.login(eq(지원하는_소셜_로그인_타입), anyString(), anyString()))
                .willThrow(new InvalidTokenException("401 Unauthorized", new RuntimeException()));

        // when & then
        mockMvc.perform(post("/oauth2/login/{oauth2Type}", 소셜_로그인_타입)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(유효하지_않은_로그인_요청))
               )
               .andExpectAll(
                       status().isUnauthorized(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 유효한_refreshToken을_전달하면_새로운_accessToken과_refreshToken을_반환한다() throws Exception {
        // given
        given(authenticationService.refreshToken(anyString())).willReturn(발급된_토큰);

        // when & then
        final ResultActions resultActions = mockMvc.perform(post("/oauth2/refresh-token")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(유효한_토큰_재발급_요청))
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.accessToken").exists(),
                                                           jsonPath("$.refreshToken").exists()
                                                   );

        refreshToken_문서화(resultActions);
    }

    @Test
    void 유효하지_않은_refreshToken을_전달하면_401을_반환한다() throws Exception {
        // given
        willThrow(new InvalidTokenException("유효한 토큰이 아닙니다.")).given(authenticationService)
                                                             .refreshToken(anyString());

        // when & then
        mockMvc.perform(post("/oauth2/refresh-token")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(유효하지_않은_토큰_재발급_요청))
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
        final ResultActions resultActions = mockMvc.perform(get("/oauth2/validate-token")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰_내용)
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.validated").value(true)
                                                   );

        validateToken_문서화(resultActions);
    }

    @Test
    void 만료된_accessToken을_검증하면_거짓을_반환한다() throws Exception {
        // given
        given(authenticationService.validateToken(anyString())).willReturn(false);

        // when & then
        mockMvc.perform(get("/oauth2/validate-token")
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, 만료된_액세스_토큰_내용)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.validated").value(false)
               );
    }

    @Test
    void accessToken과_refreshToken을_전달하면_로그아웃한다() throws Exception {
        // given
        willDoNothing().given(blackListTokenService).registerBlackListToken(anyString(), anyString());

        // when & then
        final ResultActions resultActions = mockMvc.perform(post("/oauth2/logout")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(유효한_로그아웃_요청))
                                                           .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰_내용)
                                                   )
                                                   .andExpectAll(
                                                           status().isNoContent()
                                                   );

        logout_문서화(resultActions);
    }

    @Test
    void ouath2Type과_accessToken과_refreshToken을_전달하면_탈퇴한다() throws Exception {
        // given
        willDoNothing().given(authenticationService).withdrawal(anyString(), anyString());

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/withdrawal")
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(유효한_회원탈퇴_요청))
                                                                .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰_내용)
                       )
                       .andExpectAll(
                               status().isNoContent()
                       );

        withdrawal_문서화(resultActions);
    }

    @Test
    void ouath2Type과_accessToken과_refreshToken을_전달시_이미_탈퇴_혹은_존재하지_않아_권한이_없는_회원인_경우_403을_반환한다() throws Exception {
        // given
        willThrow(new InvalidWithdrawalException("탈퇴에 대한 권한 없습니다.")).given(authenticationService)
                                                                    .withdrawal(anyString(), anyString());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.post("/oauth2/withdrawal")
                                                        .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰_내용)
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .content(objectMapper.writeValueAsString(유효하지_않은_회원탈퇴_요청))
               )
               .andExpectAll(
                       status().isForbidden(),
                       jsonPath("$.message").value("탈퇴에 대한 권한 없습니다.")
               );
    }

    private void login_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("oauth2Type").description("소셜 로그인을 할 서비스 선택(kakao로 고정)")
                        ),
                        requestFields(
                                fieldWithPath("accessToken").description("소셜 로그인 AccessToken"),
                                fieldWithPath("deviceToken").description("기기 디바이스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                                            .description("Access Token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                                             .description("Refresh Token"),
                                fieldWithPath("isSignUpUser").type(JsonFieldType.BOOLEAN)
                                                          .description("최초 로그인 여부(회원가입)")
                        )
                )
        );
    }

    private void refreshToken_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestFields(
                                fieldWithPath("refreshToken").description("refreshToken")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING)
                                                            .description("재발급한 Access Token"),
                                fieldWithPath("refreshToken").type(JsonFieldType.STRING)
                                                             .description("기존 Refresh Token")
                        )
                )
        );
    }

    private void validateToken_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        responseFields(
                                fieldWithPath("validated").type(JsonFieldType.BOOLEAN)
                                                          .description("Access Token이 유효한지 여부")
                        )
                )
        );
    }

    private void logout_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
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

    private void withdrawal_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
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
}
