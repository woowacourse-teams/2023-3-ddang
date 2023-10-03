package com.ddang.ddang.user.presentation;

import com.ddang.ddang.auction.configuration.DescendingSortPageableArgumentResolver;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.presentation.fixture.UserAuctionControllerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class UserAuctionControllerTest extends UserAuctionControllerFixture {

    TokenDecoder tokenDecoder;

    MockMvc mockMvc;

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

        mockMvc = MockMvcBuilders.standaloneSetup(userAuctionController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver, new DescendingSortPageableArgumentResolver())
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 로그인한_회원이_등록한_경매_목록을_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(auctionService.readAllByUserId(anyLong(), any(Pageable.class))).willReturn(사용자의_경매들_정보_dto);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(get("/users/auctions/mine")
                               .contentType(MediaType.APPLICATION_JSON)
                               .queryParam("size", 페이지_크기)
                               .queryParam("page", 페이지)
                               .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       )
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.auctions.[0].id", is(경매_정보_dto2.id()), Long.class),
                               jsonPath("$.auctions.[0].title", is(경매_정보_dto2.title())),
                               jsonPath("$.auctions.[0].image").exists(),
                               jsonPath("$.auctions.[0].auctionPrice", is(경매_정보_dto2.startPrice())),
                               jsonPath("$.auctions.[0].status").exists(),
                               jsonPath("$.auctions.[0].auctioneerCount", is(경매_정보_dto2.auctioneerCount())),
                               jsonPath("$.auctions.[1].id", is(경매_정보_dto1.id()), Long.class),
                               jsonPath("$.auctions.[1].title", is(경매_정보_dto1.title())),
                               jsonPath("$.auctions.[1].image").exists(),
                               jsonPath("$.auctions.[1].auctionPrice", is(경매_정보_dto1.startPrice())),
                               jsonPath("$.auctions.[1].status").exists(),
                               jsonPath("$.auctions.[1].auctioneerCount", is(경매_정보_dto1.auctioneerCount()))
                       );

        readAllByUserInfo_문서화(resultActions);
    }

    @Test
    void 로그인한_회원이_참여한_경매_목록을_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(auctionService.readAllByBidderId(anyLong(), any(Pageable.class))).willReturn(사용자가_참여한_경매들_정보_dto);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(get("/users/auctions/bids")
                               .contentType(MediaType.APPLICATION_JSON)
                               .queryParam("size", 페이지_크기)
                               .queryParam("page", 페이지)
                               .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       )
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.auctions.[0].id", is(경매_정보_dto2.id()), Long.class),
                               jsonPath("$.auctions.[0].title", is(경매_정보_dto2.title())),
                               jsonPath("$.auctions.[0].image").exists(),
                               jsonPath("$.auctions.[0].auctionPrice", is(경매_정보_dto2.startPrice())),
                               jsonPath("$.auctions.[0].status").exists(),
                               jsonPath("$.auctions.[0].auctioneerCount", is(경매_정보_dto2.auctioneerCount())),
                               jsonPath("$.auctions.[1].id", is(경매_정보_dto1.id()), Long.class),
                               jsonPath("$.auctions.[1].title", is(경매_정보_dto1.title())),
                               jsonPath("$.auctions.[1].image").exists(),
                               jsonPath("$.auctions.[1].auctionPrice", is(경매_정보_dto1.startPrice())),
                               jsonPath("$.auctions.[1].status").exists(),
                               jsonPath("$.auctions.[1].auctioneerCount", is(경매_정보_dto1.auctioneerCount()))
                       );

        readAllByBids_문서화(resultActions);
    }

    private void readAllByUserInfo_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        queryParameters(
                                parameterWithName("size").description("페이지 크기").optional(),
                                parameterWithName("page").description("페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("auctions").type(JsonFieldType.ARRAY)
                                                         .description("조회한 경매 목록"),
                                fieldWithPath("auctions.[]").type(JsonFieldType.ARRAY)
                                                            .description("조회한 단일 경매 정보"),
                                fieldWithPath("auctions.[].id").type(JsonFieldType.NUMBER)
                                                               .description("경매 ID"),
                                fieldWithPath("auctions.[].title").type(JsonFieldType.STRING)
                                                                  .description("경매 글 제목"),
                                fieldWithPath("auctions.[].image").type(JsonFieldType.STRING)
                                                                  .description("경매 대표 이미지"),
                                fieldWithPath("auctions.[].auctionPrice").type(JsonFieldType.NUMBER)
                                                                         .description("경매가(시작가, 현재가, 낙찰가 중 하나)"),
                                fieldWithPath("auctions.[].status").type(JsonFieldType.STRING)
                                                                   .description("경매 상태"),
                                fieldWithPath("auctions.[].auctioneerCount").type(JsonFieldType.NUMBER)
                                                                            .description("경매 참여자 수"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN)
                                                       .description("마지막 페이지 여부")
                        )
                )
        );
    }

    private void readAllByBids_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        queryParameters(
                                parameterWithName("size").description("페이지 크기").optional(),
                                parameterWithName("page").description("페이지 번호")
                        ),
                        responseFields(
                                fieldWithPath("auctions").type(JsonFieldType.ARRAY)
                                                         .description("조회한 경매 목록"),
                                fieldWithPath("auctions.[]").type(JsonFieldType.ARRAY)
                                                            .description("조회한 단일 경매 정보"),
                                fieldWithPath("auctions.[].id").type(JsonFieldType.NUMBER)
                                                               .description("경매 ID"),
                                fieldWithPath("auctions.[].title").type(JsonFieldType.STRING)
                                                                  .description("경매 글 제목"),
                                fieldWithPath("auctions.[].image").type(JsonFieldType.STRING)
                                                                  .description("경매 대표 이미지"),
                                fieldWithPath("auctions.[].auctionPrice").type(JsonFieldType.NUMBER)
                                                                         .description("경매가(시작가, 현재가, 낙찰가 중 하나)"),
                                fieldWithPath("auctions.[].status").type(JsonFieldType.STRING)
                                                                   .description("경매 상태"),
                                fieldWithPath("auctions.[].auctioneerCount").type(JsonFieldType.NUMBER)
                                                                            .description("경매 참여자 수"),
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN)
                                                       .description("마지막 페이지 여부")
                        )
                )
        );
    }
}
