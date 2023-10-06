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
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.dto.ReadRegionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionsDto;
import com.ddang.ddang.auction.configuration.DescendingSortPageableArgumentResolver;
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
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(controllers = {UserAuctionController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class UserAuctionControllerTest {

    @MockBean
    AuctionService auctionService;

    @MockBean
    BlackListTokenService blackListTokenService;

    @MockBean
    AuthenticationUserService authenticationUserService;

    @Autowired
    UserAuctionController userAuctionController;

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
        final ReadRegionsDto readRegionsDto = new ReadRegionsDto(
                new ReadRegionDto(1L, "서울특별시"),
                new ReadRegionDto(2L, "강서구"),
                new ReadRegionDto(3L, "역삼동")
        );
        final ReadAuctionDto auction1 = new ReadAuctionDto(
                1L,
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                List.of(1L),
                2,
                "main1",
                "sub1",
                1L,
                1L,
                "판매자",
                3.5d
        );
        final ReadAuctionDto auction2 = new ReadAuctionDto(
                2L,
                "경매 상품 2",
                "이것은 경매 상품 2 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                List.of(1L),
                2,
                "main2",
                "sub2",
                1L,
                1L,
                "판매자",
                3.5d
        );
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final ReadAuctionsDto readAuctionsDto = new ReadAuctionsDto(List.of(auction2, auction1), true);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.readAllByUserId(anyLong(), any(Pageable.class))).willReturn(readAuctionsDto);

        // when & then
        mockMvc.perform(get("/users/auctions/mine")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("size", "10")
                       .queryParam("page", "1")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.auctions.[0].id", is(auction2.id()), Long.class),
                       jsonPath("$.auctions.[0].title", is(auction2.title())),
                       jsonPath("$.auctions.[0].image").exists(),
                       jsonPath("$.auctions.[0].auctionPrice", is(auction2.startPrice())),
                       jsonPath("$.auctions.[0].status").exists(),
                       jsonPath("$.auctions.[0].auctioneerCount", is(auction2.auctioneerCount())),
                       jsonPath("$.auctions.[1].id", is(auction1.id()), Long.class),
                       jsonPath("$.auctions.[1].title", is(auction1.title())),
                       jsonPath("$.auctions.[1].image").exists(),
                       jsonPath("$.auctions.[1].auctionPrice", is(auction1.startPrice())),
                       jsonPath("$.auctions.[1].status").exists(),
                       jsonPath("$.auctions.[1].auctioneerCount", is(auction1.auctioneerCount()))
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               queryParameters(
                                       parameterWithName("size").description("페이지 크기").optional(),
                                       parameterWithName("page").description("페이지 번호")
                               ),
                               responseFields(
                                       fieldWithPath("auctions").type(JsonFieldType.ARRAY).description("조회한 경매 목록"),
                                       fieldWithPath("auctions.[]").type(JsonFieldType.ARRAY).description("조회한 단일 경매 정보"),
                                       fieldWithPath("auctions.[].id").type(JsonFieldType.NUMBER).description("경매 ID"),
                                       fieldWithPath("auctions.[].title").type(JsonFieldType.STRING).description("경매 글 제목"),
                                       fieldWithPath("auctions.[].image").type(JsonFieldType.STRING).description("경매 대표 이미지"),
                                       fieldWithPath("auctions.[].auctionPrice").type(JsonFieldType.NUMBER).description("경매가(시작가, 현재가, 낙찰가 중 하나)"),
                                       fieldWithPath("auctions.[].status").type(JsonFieldType.STRING).description("경매 상태"),
                                       fieldWithPath("auctions.[].auctioneerCount").type(JsonFieldType.NUMBER).description("경매 참여자 수"),
                                       fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                               )
                       )
               );
    }

    @Test
    void 로그인한_회원이_참여한_경매_목록을_조회한다() throws Exception {
        // given
        final ReadRegionsDto readRegionsDto = new ReadRegionsDto(
                new ReadRegionDto(1L, "서울특별시"),
                new ReadRegionDto(2L, "강서구"),
                new ReadRegionDto(3L, "역삼동")
        );
        final ReadAuctionDto auction1 = new ReadAuctionDto(
                1L,
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                List.of(1L),
                2,
                "main1",
                "sub1",
                1L,
                1L,
                "판매자",
                3.5d
        );
        final ReadAuctionDto auction2 = new ReadAuctionDto(
                2L,
                "경매 상품 2",
                "이것은 경매 상품 2 입니다.",
                1_000,
                1_000,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                List.of(1L),
                2,
                "main2",
                "sub2",
                1L,
                1L,
                "판매자",
                3.5d
        );
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final ReadAuctionsDto readAuctionsDto = new ReadAuctionsDto(List.of(auction2, auction1), true);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.readAllByBidderId(anyLong(), any(Pageable.class))).willReturn(readAuctionsDto);

        // when & then
        mockMvc.perform(get("/users/auctions/bids")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("size", "10")
                       .queryParam("page", "1")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.auctions.[0].id", is(auction2.id()), Long.class),
                       jsonPath("$.auctions.[0].title", is(auction2.title())),
                       jsonPath("$.auctions.[0].image").exists(),
                       jsonPath("$.auctions.[0].auctionPrice", is(auction2.startPrice())),
                       jsonPath("$.auctions.[0].status").exists(),
                       jsonPath("$.auctions.[0].auctioneerCount", is(auction2.auctioneerCount())),
                       jsonPath("$.auctions.[1].id", is(auction1.id()), Long.class),
                       jsonPath("$.auctions.[1].title", is(auction1.title())),
                       jsonPath("$.auctions.[1].image").exists(),
                       jsonPath("$.auctions.[1].auctionPrice", is(auction1.startPrice())),
                       jsonPath("$.auctions.[1].status").exists(),
                       jsonPath("$.auctions.[1].auctioneerCount", is(auction1.auctioneerCount()))
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               queryParameters(
                                       parameterWithName("size").description("페이지 크기").optional(),
                                       parameterWithName("page").description("페이지 번호")
                               ),
                               responseFields(
                                       fieldWithPath("auctions").type(JsonFieldType.ARRAY).description("조회한 경매 목록"),
                                       fieldWithPath("auctions.[]").type(JsonFieldType.ARRAY).description("조회한 단일 경매 정보"),
                                       fieldWithPath("auctions.[].id").type(JsonFieldType.NUMBER).description("경매 ID"),
                                       fieldWithPath("auctions.[].title").type(JsonFieldType.STRING).description("경매 글 제목"),
                                       fieldWithPath("auctions.[].image").type(JsonFieldType.STRING).description("경매 대표 이미지"),
                                       fieldWithPath("auctions.[].auctionPrice").type(JsonFieldType.NUMBER).description("경매가(시작가, 현재가, 낙찰가 중 하나)"),
                                       fieldWithPath("auctions.[].status").type(JsonFieldType.STRING).description("경매 상태"),
                                       fieldWithPath("auctions.[].auctioneerCount").type(JsonFieldType.NUMBER).description("경매 참여자 수"),
                                       fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                               )
                       )
               );
    }
}
