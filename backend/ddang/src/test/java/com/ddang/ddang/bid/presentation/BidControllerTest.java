package com.ddang.ddang.bid.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.bid.application.BidService;
import com.ddang.ddang.bid.application.dto.CreateBidDto;
import com.ddang.ddang.bid.application.dto.LoginUserDto;
import com.ddang.ddang.bid.application.dto.ReadBidDto;
import com.ddang.ddang.bid.application.exception.InvalidAuctionToBidException;
import com.ddang.ddang.bid.presentation.dto.request.CreateBidRequest;
import com.ddang.ddang.bid.presentation.resolver.LoginUserArgumentResolver;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {BidController.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class BidControllerTest {

    @MockBean
    BidService bidService;

    @Autowired
    BidController bidController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bidController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .setCustomArgumentResolvers(new LoginUserArgumentResolver())
                                 .alwaysDo(print())
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
               );
    }

    // TODO: 2023/07/30 [고민] 예외 상황들에 대해 모두 해주는 것이 좋을까요? 혹은 대표 예외 케이스(404, 400)에 대해서만 해줘도 괜찮을까요?
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
