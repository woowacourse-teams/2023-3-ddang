package com.ddang.ddang.bid.presentation;

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
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.configuration.AuthenticationStore;
import com.ddang.ddang.bid.application.dto.request.CreateBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.presentation.dto.request.CreateBidRequest;
import com.ddang.ddang.bid.presentation.fixture.BidControllerFixture;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.infrastructure.exception.UserNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("NonAsciiCharacters")
class BidControllerTest extends BidControllerFixture {

    TokenDecoder tokenDecoder;

    MockMvc mockMvc;

    BidController bidController;

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

        bidController = new BidController(bidService, urlFinder);
        mockMvc = MockMvcBuilders.standaloneSetup(bidController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .setMessageConverters(mappingJackson2HttpMessageConverter)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 입찰을_등록한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString())).willReturn(생성된_입찰_아이디);

        // when & then
        final ResultActions resultActions = mockMvc.perform(post("/bids")
                                                           .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(입찰_request))
                                                   )
                                                   .andExpectAll(
                                                           status().isCreated(),
                                                           header().string(HttpHeaders.LOCATION, is("/auctions/1"))
                                                   );

        create_문서화(resultActions);
    }

    @Test
    void 해당_경매가_없는_경우_입찰시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(존재하지_않는_경매에_대한_입찰_request))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 해당_사용자가_없는_경우_입찰시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(존재하지_않는_사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 이미_종료된_경매_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidAuctionToBidException("이미 종료된 경매입니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 이미_삭제된_경매_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidAuctionToBidException("삭제된 경매입니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 판매자가_본인_경매에_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidBidderException("판매자는 입찰할 수 없습니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 첫_입찰자가_시작가보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidBidPriceException("입찰 금액이 잘못되었습니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 마지막_입찰자가_연속으로_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidBidderException("이미 최고 입찰자입니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 최소_입찰_단위보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 범위_밖의_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(bidService.create(any(CreateBidDto.class), anyString()))
                .willThrow(new InvalidBidPriceException("입찰 금액이 잘못되었습니다"));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_아이디가_없는_경우_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(경매_아이디_없이_입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @ParameterizedTest
    @MethodSource("provideBidRequestWithNotPositiveAuctionId")
    void 경매_아이디가_양수가_아닌_값으로_입찰시_400을_반환한다(final CreateBidRequest 경매_아이디가_양수가_아닌_입찰_request) throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(경매_아이디가_양수가_아닌_입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    private static Stream<CreateBidRequest> provideBidRequestWithNotPositiveAuctionId() {
        return Stream.of(경매_아이디가_양수가_아닌_입찰_request1, 경매_아이디가_양수가_아닌_입찰_request2);
    }

    @Test
    void 입찰_금액이_없는_경우_입찰시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰액_없이_입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @ParameterizedTest
    @MethodSource("provideBidRequestWithNotPositiveBidPrice")
    void 입찰_금액이_양수가_아닌_값으로_입찰시_400을_반환한다(final CreateBidRequest 입찰액이_양수가_아닌_입찰_request) throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(입찰액이_양수가_아닌_입찰_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    private static Stream<CreateBidRequest> provideBidRequestWithNotPositiveBidPrice() {
        return Stream.of(입찰액이_양수가_아닌_입찰_request1, 입찰액이_양수가_아닌_입찰_request2);
    }

    @Test
    void 특정_경매에_대한_입찰_목록을_조회한다() throws Exception {
        // given
        given(bidService.readAllByAuctionId(anyLong())).willReturn(List.of(입찰_정보_dto1, 입찰_정보_dto2));

        // when & then
        final ResultActions resultActions = mockMvc.perform(get("/bids/{auctionId}", 조회하려는_경매_아이디)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.[0].name", is(입찰_정보_dto1.name())),
                                                           jsonPath("$.[0].profileImage").exists(),
                                                           jsonPath("$.[0].price", is(입찰_정보_dto1.price())),
                                                           jsonPath("$.[0].bidTime").exists(),
                                                           jsonPath("$.[1].name", is(입찰_정보_dto2.name())),
                                                           jsonPath("$.[1].profileImage").exists(),
                                                           jsonPath("$.[1].price", is(입찰_정보_dto2.price())),
                                                           jsonPath("$.[1].bidTime").exists()
                                                   );

        readAllByAuctionId_문서화(resultActions);
    }

    @Test
    void 존재하지_않는_경매에_대한_입찰_목록을_조회하는_경우_404를_반환한다() throws Exception {
        // given
        given(bidService.readAllByAuctionId(anyLong())).willThrow(new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/bids/{auctionId}", 존재하지_않는_경매_아이디)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    private void create_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        requestFields(
                                fieldWithPath("auctionId").description("입찰할 경매 ID"),
                                fieldWithPath("bidPrice").description("입찰 금액")
                        )
                )
        );
    }

    private void readAllByAuctionId_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                   .description("특정 경매의 모든 입찰 목록"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                                        .description("입찰한 사용자의 닉네임"),
                                fieldWithPath("[].profileImage").type(JsonFieldType.STRING)
                                                                .description("입찰한 사용자의 프로필 이미지 URL"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER)
                                                         .description("입찰한 금액"),
                                fieldWithPath("[].bidTime").type(JsonFieldType.STRING)
                                                           .description("입찰한 시간")
                        )
                )
        );
    }
}
