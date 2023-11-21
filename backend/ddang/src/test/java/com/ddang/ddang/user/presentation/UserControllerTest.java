package com.ddang.ddang.user.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.application.exception.AlreadyExistsNameException;
import com.ddang.ddang.user.infrastructure.exception.UserNotFoundException;
import com.ddang.ddang.user.presentation.fixture.UserControllerFixture;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("NonAsciiCharacters")
class UserControllerTest extends UserControllerFixture {

    TokenDecoder tokenDecoder;

    MockMvc mockMvc;

    UserController userController;

    @BeforeEach
    void setUp() {
        tokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                blackListTokenService,
                authenticationUserService,
                tokenDecoder,
                store
        );
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(store);

        userController = new UserController(userService, urlFinder);
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 사용자_정보를_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(userService.readById(anyLong())).willReturn(사용자_정보_조회_dto);

        // when & then
        final ResultActions resultActions = mockMvc.perform(get("/users")
                                                           .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.name", is(사용자_정보_조회_dto.name())),
                                                           jsonPath("$.profileImage").exists(),
                                                           jsonPath("$.reliability", is(사용자_정보_조회_dto.reliability()))
                                                   );

        readById_문서화(resultActions);
    }

    @Test
    void 탈퇴한_사용자_정보를_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(userService.readById(anyLong())).willReturn(탈퇴한_사용자_정보_조회_dto);

        // when & then
        mockMvc.perform(get("/users")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(탈퇴한_사용자_이름)),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(탈퇴한_사용자_정보_조회_dto.reliability()))
               );
    }

    @Test
    void 존재하지_않는_사용자_정보_조회시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(존재하지_않는_사용자_ID_클레임));
        given(userService.readById(anyLong())).willThrow(new UserNotFoundException("사용자 정보를 사용할 수 없습니다."));

        // when & then
        mockMvc.perform(get("/users")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 사용자_정보를_모두_수정한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(userService.updateById(anyLong(), any())).willReturn(수정후_사용자_정보_조회_dto);

        // when & then
        final ResultActions resultActions = mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                                                           .file(수정할_이름)
                                                           .file(수정할_프로필_이미지)
                                                           .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                           .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.name", is(수정할_이름_request.name())),
                                                           jsonPath("$.profileImage").exists(),
                                                           jsonPath("$.reliability", is(사용자_정보_조회_dto.reliability()))
                                                   );

        updateById_문서화(resultActions);
    }

    @Test
    void 사용자_정보를_이름만_수정한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(userService.updateById(anyLong(), any())).willReturn(수정후_사용자_정보_조회_dto);

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(수정할_이름)
                       .file(프로필_이미지가_없는_경우_파일)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(수정할_이름_request.name())),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(사용자_정보_조회_dto.reliability()))
               );
    }

    @Test
    void 사용자_정보를_이미지만_수정한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(userService.updateById(anyLong(), any())).willReturn(사용자_정보_조회_dto);

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(이름을_수정하지_않는_경우)
                       .file(수정할_프로필_이미지)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(사용자_정보_조회_dto.name())),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(사용자_정보_조회_dto.reliability()))
               );
    }

    @Test
    void 이미_존재하는_이름으로_수정할시_400_예외를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(userService.updateById(anyLong(), any())).willThrow(new AlreadyExistsNameException("이미 존재하는 닉네임입니다."));

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(수정할_이름)
                       .file(프로필_이미지가_없는_경우_파일)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 존재하지_않는_사용자_정보를_수정하면_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(존재하지_않는_사용자_ID_클레임));
        given(userService.updateById(anyLong(), any())).willThrow(new UserNotFoundException("사용자 정보를 사용할 수 없습니다."));

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(수정할_이름)
                       .file(프로필_이미지가_없는_경우_파일)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    private void readById_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                                     .description("사용자 닉네임"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING)
                                                             .description("사용자 프로필 이미지"),
                                fieldWithPath("reliability").type(JsonFieldType.NUMBER)
                                                            .description("사용자 신뢰도")
                        )
                )
        );
    }

    private void updateById_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        requestParts(
                                partWithName("profileImage").description("수정할 프로필 이미지 파일"),
                                partWithName("request").description("요청 데이터 - 수정할 이름")
                        ),
                        responseFields(
                                fieldWithPath("name").type(JsonFieldType.STRING)
                                                     .description("사용자 닉네임"),
                                fieldWithPath("profileImage").type(JsonFieldType.STRING)
                                                             .description("사용자 프로필 이미지"),
                                fieldWithPath("reliability").type(JsonFieldType.NUMBER)
                                                            .description("사용자 신뢰도")
                        )
                )
        );
    }
}
