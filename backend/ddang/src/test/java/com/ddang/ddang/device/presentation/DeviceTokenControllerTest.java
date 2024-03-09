package com.ddang.ddang.device.presentation;

import com.ddang.ddang.auction.configuration.DescendingSortPageableArgumentResolver;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptorService;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.device.application.dto.PersistDeviceTokenDto;
import com.ddang.ddang.device.presentation.fixture.DeviceTokenControllerFixture;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenControllerTest extends DeviceTokenControllerFixture {

    TokenDecoder tokenDecoder;
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        tokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                new AuthenticationInterceptorService(
                        blackListTokenService,
                        authenticationUserService,
                        tokenDecoder,
                        store
                )
        );
        final AuthenticationPrincipalArgumentResolver resolver = new AuthenticationPrincipalArgumentResolver(store);

        mockMvc = MockMvcBuilders.standaloneSetup(deviceTokenController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver, new DescendingSortPageableArgumentResolver())
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 디바이스_토큰을_저장_또는_갱신한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_ID_클레임));
        doNothing().when(deviceTokenService).persist(anyLong(), any(PersistDeviceTokenDto.class));

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(patch("/device-token")
                               .contentType(MediaType.APPLICATION_JSON)
                               .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                               .content(objectMapper.writeValueAsString(디바이스_토큰_갱신_요청)))
                       .andExpectAll(
                               status().isOk()
                       );

        update_문서화(resultActions);
    }

    @Test
    void 사용자를_찾을_수_없는_경우_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효하지_않은_사용자_ID_클레임));
        willThrow(new UserNotFoundException("해당 사용자를 찾을 수 없습니다."))
                .given(deviceTokenService).persist(anyLong(), any(PersistDeviceTokenDto.class));

        // when & then
        mockMvc.perform(patch("/device-token")
                       .header(HttpHeaders.AUTHORIZATION, 유효하지_않은_액세스_토큰_값)
                       .content(objectMapper.writeValueAsString(디바이스_토큰_갱신_요청))
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    private void update_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        requestFields(
                                fieldWithPath("deviceToken").description("디바이스 토큰")
                        )
                )
        );
    }
}
