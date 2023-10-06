package com.ddang.ddang.user.presentation;

import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.application.UserService;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.presentation.dto.request.UpdateUserRequest;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Optional;

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

@WebMvcTest(controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class UserControllerTest {

    @MockBean
    UserService userService;

    @MockBean
    BlackListTokenService blackListTokenService;

    @MockBean
    AuthenticationUserService authenticationUserService;

    @Autowired
    UserController userController;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Autowired
    ObjectMapper objectMapper;

    TokenDecoder mockTokenDecoder;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired RestDocumentationContextProvider provider) {
        mockTokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                blackListTokenService,
                authenticationUserService,
                mockTokenDecoder,
                store
        );
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(store);

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
        final ReadUserDto readUserDto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", false);
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(userService.readById(anyLong())).willReturn(readUserDto);

        // when & then
        mockMvc.perform(get("/users")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(readUserDto.name())),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(readUserDto.reliability()))
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               responseFields(
                                       fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 닉네임"),
                                       fieldWithPath("profileImage").type(JsonFieldType.STRING)
                                                                    .description("사용자 프로필 이미지"),
                                       fieldWithPath("reliability").type(JsonFieldType.NUMBER).description("사용자 신뢰도")
                               )
                       )
               );
    }

    @Test
    void 탈퇴한_사용자_정보를_조회한다() throws Exception {
        // given
        final ReadUserDto readUserDto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", true);
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(userService.readById(anyLong())).willReturn(readUserDto);

        // when & then
        mockMvc.perform(get("/users")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is("알 수 없음")),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(readUserDto.reliability()))
        );
    }

    @Test
    void 사용자_정보를_모두_수정한다() throws Exception {
        // given
        final MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest("updateName");
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(updateUserRequest)
        );

        final ReadUserDto readUserDto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", false);
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(userService.updateById(anyLong(), any())).willReturn(readUserDto);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(request)
                       .file(profileImage)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(readUserDto.name())),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(readUserDto.reliability()))
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               requestParts(
                                       partWithName("profileImage").description("수정할 프로필 이미지 파일"),
                                       partWithName("request").description("요청 데이터 - 수정할 이름")
                               ),
                               responseFields(
                                       fieldWithPath("name").type(JsonFieldType.STRING).description("사용자 닉네임"),
                                       fieldWithPath("profileImage").type(JsonFieldType.STRING).description("사용자 프로필 이미지"),
                                       fieldWithPath("reliability").type(JsonFieldType.NUMBER).description("사용자 신뢰도")
                               )
                       )
               );
    }

    @Test
    void 사용자_정보를_이름만_수정한다() throws Exception {
        // given
        final MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                (byte[]) null
        );
        final UpdateUserRequest updateUserRequest = new UpdateUserRequest("updateName");
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(updateUserRequest)
        );

        final ReadUserDto readUserDto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", false);
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(userService.updateById(anyLong(), any())).willReturn(readUserDto);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(request)
                       .file(profileImage)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(readUserDto.name())),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(readUserDto.reliability()))
               );
    }

    @Test
    void 사용자_정보를_이미지만_수정한다() throws Exception {
        // given
        final MockMultipartFile profileImage = new MockMultipartFile(
                "profileImage",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                (byte[]) null
        );

        final ReadUserDto readUserDto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", false);
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(userService.updateById(anyLong(), any())).willReturn(readUserDto);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(multipart(HttpMethod.PATCH, "/users")
                       .file(request)
                       .file(profileImage)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.name", is(readUserDto.name())),
                       jsonPath("$.profileImage").exists(),
                       jsonPath("$.reliability", is(readUserDto.reliability()))
               );
    }

    @Test
    void 존재하지_않는_사용자_정보_조회시_404를_반환한다() throws Exception {
        // given
        final UserNotFoundException userNotFoundException = new UserNotFoundException("사용자 정보를 사용할 수 없습니다.");
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(userService.readById(anyLong())).willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(get("/users")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }
}
