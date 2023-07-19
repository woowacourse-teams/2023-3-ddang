package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.presentation.dto.CreateAuctionRequest;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {AuctionController.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionControllerTest {

    @MockBean
    AuctionService auctionService;

    @Autowired
    AuctionController auctionController;

    @Autowired
    ObjectMapper objectMapper;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auctionController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .alwaysDo(print())
                                 .build();
    }

    @Test
    void 경매를_등록한다() throws Exception {
        // given
        final CreateAuctionRequest request = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now()
                             .plusDays(3L),
                // TODO 2차 데모데이 이후 리펙토링 예정
                "",
                "",
                "",
                "",
                "",
                ""
        );

        given(auctionService.create(any(CreateAuctionDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(post("/auctions").contentType(MediaType.APPLICATION_JSON)
                                         .content(objectMapper.writeValueAsString(request))
               )
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1")),
                       jsonPath("$.id", is(1L), Long.class)
               );
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_조회한다() throws Exception {
        // given
        final ReadAuctionDto auction = new ReadAuctionDto(
                1L,
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                null,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                // TODO 2차 데모데이 이후 리펙토링 예정
                "",
                "",
                "",
                "",
                "",
                ""
        );

        given(auctionService.readByAuctionId(anyLong())).willReturn(auction);

        // when & then
        mockMvc.perform(get("/auctions/{auctionId}", auction.id()).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.auction.id", is(auction.id()), Long.class),
                       jsonPath("$.auction.title", is(auction.title())),
                       jsonPath("$.auction.description", is(auction.description())),
                       jsonPath("$.auction.bidUnit", is(auction.bidUnit())),
                       jsonPath("$.auction.startBidPrice", is(auction.startBidPrice())),
                       jsonPath("$.auction.deleted", is(auction.deleted())),
                       jsonPath("$.auction.registerTime").exists(),
                       jsonPath("$.auction.closingTime").exists()
               );
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_삭제한다() throws Exception {
        // given
        willDoNothing().given(auctionService).deleteByAuctionId(anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", 1L).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(status().isNoContent());
    }
}
