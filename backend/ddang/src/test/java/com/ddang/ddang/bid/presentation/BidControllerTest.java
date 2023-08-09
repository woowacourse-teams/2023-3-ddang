package com.ddang.ddang.bid.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.application.exception.InvalidBidPriceException;
import com.ddang.ddang.bid.application.exception.InvalidBidderException;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.bid.presentation.dto.request.CreateBidRequest;
import com.ddang.ddang.bid.presentation.resolver.LoginUserArgumentResolver;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
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

@WebMvcTest(controllers = {BidController.class})
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidControllerTest {

    @MockBean
    BidService bidService;

    @Autowired
    BidController bidController;

    @Autowired
    RestDocumentationResultHandler restDocs;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp(@Autowired RestDocumentationContextProvider provider) {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setCustomArgumentResolvers(new LoginUserArgumentResolver())
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 입찰을_등록한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);

        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1"))
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("로그인한 사용자 ID")
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
        final Long invalidAuctionId = 9999L;

        final CreateBidRequest bidRequest = new CreateBidRequest(invalidAuctionId, 10_000);
        final AuctionNotFoundException auctionNotFoundException = new AuctionNotFoundException("해당 경매를 찾을 수 없습니다.");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(auctionNotFoundException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }

    @Test
    void 해당_사용자가_없는_경우_입찰시_404를_반환한다() throws Exception {
        // given
        final Long invalidUserId = 9999L;

        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final UserNotFoundException userNotFoundException = new UserNotFoundException("해당 사용자를 찾을 수 없습니다.");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(userNotFoundException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", invalidUserId)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(userNotFoundException.getMessage()))
               );
    }

    @Test
    void 이미_종료된_경매_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidAuctionToBidException invalidAuctionToBidException = new InvalidAuctionToBidException("이미 종료된 경매입니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidAuctionToBidException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidAuctionToBidException.getMessage()))
               );
    }

    @Test
    void 이미_삭제된_경매_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidAuctionToBidException invalidAuctionToBidException = new InvalidAuctionToBidException("삭제된 경매입니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidAuctionToBidException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidAuctionToBidException.getMessage()))
               );
    }

    @Test
    void 판매자가_본인_경매에_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidderException invalidBidderException = new InvalidBidderException("판매자는 입찰할 수 없습니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidBidderException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidderException.getMessage()))
               );
    }

    @Test
    void 첫_입찰자가_시작가_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
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
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidderException invalidBidderException = new InvalidBidderException("이미 최고 입찰자입니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidBidderException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidderException.getMessage()))
               );
    }

    @Test
    void 마지막_입찰액보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 최소_입찰_단위보다_낮은_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 10_000);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("가능 입찰액보다 낮은 금액을 입력했습니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 범위_밖의_금액으로_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, 2_100_000_001);
        final InvalidBidPriceException invalidBidPriceException = new InvalidBidPriceException("입찰 금액이 잘못되었습니다");
        given(bidService.create(any(LoginUserDto.class), any(CreateBidDto.class)))
                .willThrow(invalidBidPriceException);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is(invalidBidPriceException.getMessage()))
               );
    }

    @Test
    void 경매_아이디가_없는_경우_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(null, 10_000);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디가 입력되지 않았습니다."))
               );
    }

    @ParameterizedTest
    @ValueSource(longs = {-1L, 0L})
    void 경매_아이디가_양수가_아닌_값으로_입찰시_400을_반환한다(final Long auctionId) throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(auctionId, 10_000);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("경매 아이디는 양수입니다."))
               );
    }

    @Test
    void 입찰_금액이_없는_경우_입찰시_400을_반환한다() throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, null);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("입찰 금액이 입력되지 않았습니다."))
               );
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0})
    void 입찰_금액이_양수가_아닌_값으로_입찰시_400을_반환한다(final Integer bidPrice) throws Exception {
        // given
        final CreateBidRequest bidRequest = new CreateBidRequest(1L, bidPrice);

        // when & then
        mockMvc.perform(post("/bids").header("Authorization", 1L)
                                     .contentType(MediaType.APPLICATION_JSON)
                                     .content(objectMapper.writeValueAsString(bidRequest)))
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message", is("입찰 금액은 양수입니다."))
               );
    }

    @Test
    void 특정_경매에_대한_입찰_목록을_조회한다() throws Exception {
        // given
        final ReadBidDto bid1 = new ReadBidDto("사용자1", "이미지1", 10_000, LocalDateTime.now());
        final ReadBidDto bid2 = new ReadBidDto("사용자2", "이미지2", 12_000, LocalDateTime.now());

        given(bidService.readAllByAuctionId(anyLong())).willReturn(List.of(bid1, bid2));

        // when & then
        mockMvc.perform(get("/bids/{auctionId}", 1L).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.bids.[0].name", is(bid1.name())),
                       jsonPath("$.bids.[0].profileImage", is(bid1.profileImage())),
                       jsonPath("$.bids.[0].price", is(bid1.price())),
                       jsonPath("$.bids.[0].bidTime").exists(),
                       jsonPath("$.bids.[1].name", is(bid2.name())),
                       jsonPath("$.bids.[1].profileImage", is(bid2.profileImage())),
                       jsonPath("$.bids.[1].price", is(bid2.price())),
                       jsonPath("$.bids.[1].bidTime").exists()
               )
               .andDo(
                       restDocs.document(
                               responseFields(
                                       fieldWithPath("bids.[]").type(JsonFieldType.ARRAY)
                                                               .description("특정 경매의 모든 입찰 목록"),
                                       fieldWithPath("bids.[].name").type(JsonFieldType.STRING)
                                                                    .description("입찰한 사용자의 닉네임"),
                                       fieldWithPath("bids.[].profileImage").type(JsonFieldType.STRING)
                                                                            .description("입찰한 사용자의 프로필 이미지 URL"),
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
        mockMvc.perform(get("/bids/{auctionId}", invalidAuctionId).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(auctionNotFoundException.getMessage()))
               );
    }
}
