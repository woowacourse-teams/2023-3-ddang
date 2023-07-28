package com.ddang.ddang.auction.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionsDto;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1}
        );

        given(auctionService.create(any(CreateAuctionDto.class))).willReturn(1L);

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .param("title", "경매 상품 1")
                       .param("description", "이것은 경매 상품 1 입니다.")
                       .param("bidUnit", "1000")
                       .param("startPrice", "1000")
                       .param("closingTime", LocalDateTime.now().plusDays(3L).toString())
                       .param("subCategoryId", "2")
                       .param("thirdRegionIds", "3")
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
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
        final ReadRegionsDto readRegionsDto = new ReadRegionsDto(
                new ReadRegionDto(1L, "서울특별시"),
                new ReadRegionDto(2L, "강서구"),
                new ReadRegionDto(3L, "역삼동")
        );
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
                List.of(readRegionsDto),
                List.of(1L),
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
                       jsonPath("$.auction.startPrice", is(auction.startPrice())),
                       jsonPath("$.auction.registerTime").exists(),
                       jsonPath("$.auction.closingTime").exists()
               );
    }

    @Test
    void 첫번째_페이지의_경매_목록을_조회한다() throws Exception {
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
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                // TODO 2차 데모데이 이후 리펙토링 예정
                List.of(readRegionsDto),
                List.of(1L),
                "",
                ""
        );
        final ReadAuctionDto auction2 = new ReadAuctionDto(
                2L,
                "경매 상품 2",
                "이것은 경매 상품 2 입니다.",
                1_000,
                1_000,
                null,
                null,
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                // TODO 2차 데모데이 이후 리펙토링 예정
                List.of(1L),
                "main2",
                "sub2"
        );

        given(auctionService.readAllByLastAuctionId(any(), anyInt())).willReturn(List.of(auction2, auction1));

        // when & then
        mockMvc.perform(get("/auctions").contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.auctions.[0].id", is(auction2.id()), Long.class),
                       jsonPath("$.auctions.[0].title", is(auction2.title())),
                       jsonPath("$.auctions.[0].image").exists(),
                       jsonPath("$.auctions.[0].auctionPrice", is(auction2.startPrice())),
                       jsonPath("$.auctions.[0].status").exists(),
                       jsonPath("$.auctions.[0].auctioneerCount").exists(),
                       jsonPath("$.auctions.[1].id", is(auction1.id()), Long.class),
                       jsonPath("$.auctions.[1].title", is(auction1.title())),
                       jsonPath("$.auctions.[1].image").exists(),
                       jsonPath("$.auctions.[1].auctionPrice", is(auction1.startPrice())),
                       jsonPath("$.auctions.[1].status").exists(),
                       jsonPath("$.auctions.[1].auctioneerCount").exists()
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
