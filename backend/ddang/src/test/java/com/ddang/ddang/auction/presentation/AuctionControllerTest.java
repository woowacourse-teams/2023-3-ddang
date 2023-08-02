package com.ddang.ddang.auction.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ddang.ddang.auction.application.AuctionService;
import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.dto.CreateInfoAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.dto.ReadRegionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionsDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.bid.application.exception.UserNotFoundException;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
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
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );
        final CreateInfoAuctionDto createInfoAuctionDto = new CreateInfoAuctionDto(
                1L,
                "title",
                1L,
                1_000
        );

        given(auctionService.create(any(CreateAuctionDto.class))).willReturn(createInfoAuctionDto);

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1")),
                       jsonPath("$.id", is(1L), Long.class)
               );
    }

    @Test
    void 경매_등록시_유효한_회원이_아니라면_404을_반환한다() throws Exception {
        // given
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );

        given(auctionService.create(any())).willThrow(new UserNotFoundException("지정한 판매자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_유효한_하위_카테고리가_아니라면_400을_반환한다() throws Exception {
        // given
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );

        given(auctionService.create(any())).willThrow(
                new CategoryNotFoundException("지정한 판매자를 찾을 수 없습니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_유효한_세번째_지역이_아니라면_404을_반환한다() throws Exception {
        // given
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );

        given(auctionService.create(any())).willThrow(
                new RegionNotFoundException("지정한 세 번째 지역이 없거나 세 번째 지역이 아닙니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_이미지_파일의_데이터가_비어있다면_400을_반환한다() throws Exception {
        // given
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );

        given(auctionService.create(any())).willThrow(
                new EmptyImageException("이미지 파일의 데이터가 비어 있습니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_이미지_저장에_실패하면_500을_반환한다() throws Exception {
        // given
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );

        given(auctionService.create(any())).willThrow(
                new StoreImageFailureException("이미지 저장에 실패했습니다.", null)
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isInternalServerError(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_지원하지_않는_확장자의_이미지면_400을_반환한다() throws Exception {
        // given
        final MockMultipartFile auctionImage = new MockMultipartFile(
                "images",
                "image.png",
                MediaType.IMAGE_PNG_VALUE,
                new byte[]{1}
        );
        final CreateAuctionRequest createAuctionRequest = new CreateAuctionRequest(
                "경매 상품 1",
                "이것은 경매 상품 1 입니다.",
                1_000,
                1_000,
                LocalDateTime.now().plusDays(3L),
                2L,
                List.of(3L)
        );
        final MockMultipartFile request = new MockMultipartFile(
                "request",
                "request",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(createAuctionRequest)
        );

        given(auctionService.create(any())).willThrow(
                new UnsupportedImageFileExtensionException("지원하지 않는 확장자입니다. : ")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
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
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                List.of(1L),
                "main",
                "sub",
                1L,
                "https://profile.com",
                "판매자",
                3.5d
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
    void 경매_조회시_지정한_아이디에_해당하는_경매가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;

        given(auctionService.readByAuctionId(anyLong())).willThrow(new AuctionNotFoundException(
                "지정한 아이디에 대한 경매를 찾을 수 없습니다."
        ));

        // when & then
        mockMvc.perform(get("/auctions/{auctionId}", invalidAuctionId).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
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
                false,
                LocalDateTime.now(),
                LocalDateTime.now(),
                List.of(readRegionsDto),
                List.of(1L),
                "main1",
                "sub1",
                1L,
                "https://profile.com",
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
                "main2",
                "sub2",
                1L,
                "https://profile.com",
                "판매자",
                3.5d
        );

        final ReadAuctionsDto readAuctionsDto = new ReadAuctionsDto(List.of(auction2, auction1), true);
        given(auctionService.readAllByLastAuctionId(any(), anyInt())).willReturn(readAuctionsDto);

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
        willDoNothing().given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", 1L).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(status().isNoContent());
    }

    @Test
    void 경매_삭제시_지정한_아이디에_해당하는_경매가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;

        willThrow(new AuctionNotFoundException("지정한 아이디에 대한 경매를 찾을 수 없습니다."))
                .given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", invalidAuctionId).contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_삭제시_유효한_회원이_아니라면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;

        willThrow(new UserNotFoundException("회원 정보를 찾을 수 없습니다."))
                .given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", invalidAuctionId)
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }
}
