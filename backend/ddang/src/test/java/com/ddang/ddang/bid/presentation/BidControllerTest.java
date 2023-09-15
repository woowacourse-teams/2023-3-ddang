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

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.bid.presentation.dto.request.CreateBidRequest;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(controllers = {BidController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidControllerTest {

    @MockBean
    BidService bidService;

    @MockBean
    BlackListTokenService blackListTokenService;

    @MockBean
    AuthenticationUserService authenticationUserService;

    @Autowired
    BidController bidController;

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

        mockMvc = MockMvcBuilders.standaloneSetup(bidController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 입찰을_등록한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1"))
               )
               .andDo(
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

    // TODO: 2023-08-06 예외 케이스 api 문서화의 경우 예외에 대한 변경이 없을 때 추가할 것
    @Test
    void 해당_경매가_없는_경우_입찰시_404를_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final Long invalidAuctionId = 9999L;
        final CreateBidRequest bidRequest = new CreateBidRequest(invalidAuctionId, 10_000);
        final AuctionNotFoundException auctionNotFoundException = new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class))).willThrow(auctionNotFoundException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }

    @Test
    void 해당_사용자가_없는_경우_입찰시_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = 9999L;
        final PrivateClaims privateClaims = new PrivateClaims(invalidUserId);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final UserNotFoundException userNotFoundException = new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 이미_종료된_경매_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidAuctionToBidException invalidAuctionToBidException = new InvalidAuctionToBidException("이미 종료된 경매입니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidAuctionToBidException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidAuctionToBidException.getMessage()))
               );
    }

    @Test
    void 이미_삭제된_경매_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidAuctionToBidException invalidAuctionToBidException = new InvalidAuctionToBidException("삭제된 경매입니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidAuctionToBidException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidAuctionToBidException.getMessage()))
               );
    }

    @Test
    void 판매자가_본인_경매에_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidderException invalidBidderException = new InvalidBidderException("판매자는 입찰할 수 없습니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidBidderException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidderException.getMessage()))
               );
    }

    @Test
    void 첫_입찰자가_시작가_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("입찰 금액이 잘못되었습니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 마지막_입찰자가_연속으로_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidderException invalidBidderException = new InvalidBidderException("이미 최고 입찰자입니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidBidderException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidderException.getMessage()))
               );
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 최소_입찰_단위보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 범위_밖의_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 2_100_000_001);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("입찰 금액이 잘못되었습니다");

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(bidService.create(any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 경매_아이디가_없는_경우_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(null, 10_000);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디가 입력되지 않았습니다."))
               );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    void 경매_아이디가_양수가_아닌_값으로_입찰시_400을_반환한다(final Long auctionId) throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(auctionId, 10_000);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디는 양수입니다."))
               );
    }

    @Test
    void 입찰_금액이_없는_경우_입찰시_400을_반환한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, null);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("입찰 금액이 입력되지 않았습니다."))
               );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 입찰_금액이_양수가_아닌_값으로_입찰시_400을_반환한다(final Integer bidPrice) throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, bidPrice);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(post("/bids")
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(bidRequest))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("입찰 금액은 양수입니다."))
               );
    }

    @Test
    void 특정_경매에_대한_입찰_목록을_조회한다() throws Exception {
        // given
        final ReadBidDto bid1 = new ReadBidDto("사용자1", 1L, false, 10_000, LocalDateTime.now());
        final ReadBidDto bid2 = new ReadBidDto("사용자2", 2L, false, 12_000, LocalDateTime.now());

        given(bidService.readAllByAuctionId(anyLong())).willReturn(List.of(bid1, bid2));

        // when & then
        mockMvc.perform(get("/bids/{auctionId}", 1L)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.bids.[0].name", is(bid1.name())),
                       jsonPath("$.bids.[0].profileImage").exists(),
                       jsonPath("$.bids.[0].price", is(bid1.price())),
                       jsonPath("$.bids.[0].bidTime").exists(),
                       jsonPath("$.bids.[1].name", is(bid2.name())),
                       jsonPath("$.bids.[1].profileImage").exists(),
                       jsonPath("$.bids.[1].price", is(bid2.price())),
                       jsonPath("$.bids.[1].bidTime").exists()
               )
               .andDo(
                       restDocs.document(
                               responseFields(
                                       fieldWithPath("bids.[]").type(JsonFieldType.ARRAY).description("특정 경매의 모든 입찰 목록"),
                                       fieldWithPath("bids.[].name").type(JsonFieldType.STRING).description("입찰한 사용자의 닉네임"),
                                       fieldWithPath("bids.[].profileImage").type(JsonFieldType.STRING).description("입찰한 사용자의 프로필 이미지 URL"),
                                       fieldWithPath("bids.[].price").type(JsonFieldType.NUMBER).description("입찰한 금액"),
                                       fieldWithPath("bids.[].bidTime").type(JsonFieldType.STRING).description("입찰한 시간")
                               )
                       )
               );
    }

    @Test
    void 존재하지_않는_경매에_대한_입찰_목록을_조회하는_경우_404를_반환한다() throws Exception {
        // given
        final AuctionNotFoundException auctionNotFoundException = new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");
        given(bidService.readAllByAuctionId(anyLong()))
                .willThrow(auctionNotFoundException);

        // when & then
        final Long invalidAuctionId = -999L;
        mockMvc.perform(get("/bids/{auctionId}", invalidAuctionId)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }
}
