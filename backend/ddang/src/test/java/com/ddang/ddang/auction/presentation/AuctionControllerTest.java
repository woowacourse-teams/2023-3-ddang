package com.ddang.ddang.auction.presentation;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
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
import com.ddang.ddang.auction.application.dto.ReadAuctionWithChatRoomIdDto;
import com.ddang.ddang.auction.application.dto.ReadAuctionsDto;
import com.ddang.ddang.auction.application.dto.ReadChatRoomDto;
import com.ddang.ddang.auction.application.dto.ReadRegionDto;
import com.ddang.ddang.auction.application.dto.ReadRegionsDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.configuration.DescendingSortPageableArgumentResolver;
import com.ddang.ddang.auction.presentation.dto.request.CreateAuctionRequest;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.authentication.application.AuthenticationUserService;
import com.ddang.ddang.authentication.application.BlackListTokenService;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.authentication.domain.dto.AuthenticationUserInfo;
import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.configuration.RestDocsConfiguration;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@WebMvcTest(controllers = {AuctionController.class},
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebMvcConfigurer.class),
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com\\.ddang\\.ddang\\.authentication\\.configuration\\..*")
        }
)
@AutoConfigureRestDocs
@Import(RestDocsConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AuctionControllerTest {

    @MockBean
    AuctionService auctionService;

    @MockBean
    BlackListTokenService blackListTokenService;

    @MockBean
    AuthenticationUserService authenticationUserService;

    @Autowired
    AuctionController auctionController;

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

        mockMvc = MockMvcBuilders.standaloneSetup(auctionController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver, new DescendingSortPageableArgumentResolver())
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(auctionService.create(any(CreateAuctionDto.class))).willReturn(createInfoAuctionDto);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isCreated(),
                       header().string(HttpHeaders.LOCATION, is("/auctions/1")),
                       jsonPath("$.id", is(1L), Long.class)
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               requestParts(
                                       partWithName("images").description("이미지 파일(Array, 최대 10장)"),
                                       partWithName("request").description("요청 데이터")
                               ),
                               responseFields(
                                       fieldWithPath("id").type(JsonFieldType.NUMBER).description("경매 ID"),
                                       fieldWithPath("title").type(JsonFieldType.STRING).description("경매 글 제목"),
                                       fieldWithPath("image").type(JsonFieldType.STRING).description("경매 대표 이미지"),
                                       fieldWithPath("auctionPrice").type(JsonFieldType.NUMBER).description("시작가"),
                                       fieldWithPath("status").type(JsonFieldType.STRING).description("경매 상태"),
                                       fieldWithPath("auctioneerCount").type(JsonFieldType.NUMBER).description("경매 참여자 수")
                               )
                       )
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.create(any())).willThrow(new UserNotFoundException("지정한 판매자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.create(any())).willThrow(
                new CategoryNotFoundException("지정한 판매자를 찾을 수 없습니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.create(any())).willThrow(
                new RegionNotFoundException("지정한 세 번째 지역이 없거나 세 번째 지역이 아닙니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.create(any())).willThrow(
                new EmptyImageException("이미지 파일의 데이터가 비어 있습니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.create(any())).willThrow(
                new StoreImageFailureException("이미지 저장에 실패했습니다.", null)
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.create(any())).willThrow(
                new UnsupportedImageFileExtensionException("지원하지 않는 확장자입니다. : ")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(auctionImage)
                       .file(request)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
                2,
                "main",
                "sub",
                1L,
                "https://profile.com",
                "판매자",
                3.5d
        );
        final ReadChatRoomDto chatRoomDto = new ReadChatRoomDto(1L, true);

        final ReadAuctionWithChatRoomIdDto auctionWithChatRoomIdDto =
                new ReadAuctionWithChatRoomIdDto(auction, chatRoomDto);
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.readByAuctionId(anyLong(), any(AuthenticationUserInfo.class)))
                .willReturn(auctionWithChatRoomIdDto);

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/{auctionId}", auction.id())
                                                        .contentType(MediaType.APPLICATION_JSON)
                                                        .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.auction.id", is(auction.id()), Long.class),
                       jsonPath("$.auction.title", is(auction.title())),
                       jsonPath("$.auction.description", is(auction.description())),
                       jsonPath("$.auction.bidUnit", is(auction.bidUnit())),
                       jsonPath("$.auction.startPrice", is(auction.startPrice())),
                       jsonPath("$.auction.registerTime").exists(),
                       jsonPath("$.auction.closingTime").exists(),
                       jsonPath("$.auction.auctioneerCount", is(auction.auctioneerCount()))
               )
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               pathParameters(
                                       parameterWithName("auctionId").description("조회하고자 하는 경매 ID")
                               ),
                               responseFields(
                                       fieldWithPath("auction.id").type(JsonFieldType.NUMBER).description("경매 글 ID"),
                                       fieldWithPath("auction.images").type(JsonFieldType.ARRAY).description("경매 이미지"),
                                       fieldWithPath("auction.title").type(JsonFieldType.STRING).description("경매 글 제목"),
                                       fieldWithPath("auction.category").type(JsonFieldType.OBJECT).description("경매 카테고리"),
                                       fieldWithPath("auction.category.main").type(JsonFieldType.STRING).description("상위 카테고리"),
                                       fieldWithPath("auction.category.sub").type(JsonFieldType.STRING).description("하위 카테고리"),
                                       fieldWithPath("auction.description").type(JsonFieldType.STRING).description("경매 본문"),
                                       fieldWithPath("auction.startPrice").type(JsonFieldType.NUMBER).description("시작가"),
                                       fieldWithPath("auction.lastBidPrice").description("마지막 입찰가"),
                                       fieldWithPath("auction.status").description("경매 상태"),
                                       fieldWithPath("auction.bidUnit").type(JsonFieldType.NUMBER).description("입찰 단위"),
                                       fieldWithPath("auction.registerTime").type(JsonFieldType.STRING).description("경매 등록시간"),
                                       fieldWithPath("auction.closingTime").type(JsonFieldType.STRING).description("경매 마감시간"),
                                       fieldWithPath("auction.directRegions").type(JsonFieldType.ARRAY).description("모든 직거래 지역"),
                                       fieldWithPath("auction.directRegions.[]").type(JsonFieldType.ARRAY).description("단일 직거래 지역"),
                                       fieldWithPath("auction.directRegions.[].first").type(JsonFieldType.STRING).description("첫 번째 직거래 지역"),
                                       fieldWithPath("auction.directRegions.[].second").type(JsonFieldType.STRING).description("두 번째 직거래 지역"),
                                       fieldWithPath("auction.directRegions.[].third").type(JsonFieldType.STRING).description("세 번째 직거래 지역"),
                                       fieldWithPath("auction.auctioneerCount").type(JsonFieldType.NUMBER).description("경매 참여자 수"),
                                       fieldWithPath("seller").type(JsonFieldType.OBJECT).description("판매자 정보"),
                                       fieldWithPath("seller.id").type(JsonFieldType.NUMBER).description("판매자 ID"),
                                       fieldWithPath("seller.image").type(JsonFieldType.STRING).description("판매자 프로필 이미지 주소"),
                                       fieldWithPath("seller.nickname").type(JsonFieldType.STRING).description("판매자 닉네임"),
                                       fieldWithPath("seller.reliability").type(JsonFieldType.NUMBER).description("판매자 신뢰도"),
                                       fieldWithPath("chat.id").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                       fieldWithPath("chat.isChatParticipant").type(JsonFieldType.BOOLEAN).description("채팅방을 생성 가능 유저 여부"),
                                       fieldWithPath("isOwner").type(JsonFieldType.BOOLEAN).description("유저가 해당 경매 글을 작성한 유저인지에 대한 여부")
                               )
                       )
               );
    }

    @Test
    void 경매_조회시_지정한_아이디에_해당하는_경매가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(auctionService.readByAuctionId(anyLong(), any(AuthenticationUserInfo.class)))
                .willThrow(new AuctionNotFoundException("지정한 아이디에 대한 경매를 찾을 수 없습니다."));
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));

        // when & then
        mockMvc.perform(get("/auctions/{auctionId}", invalidAuctionId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken"))
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
                2,
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
                2,
                "main2",
                "sub2",
                1L,
                "https://profile.com",
                "판매자",
                3.5d
        );
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final ReadAuctionsDto readAuctionsDto = new ReadAuctionsDto(List.of(auction2, auction1), true);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.readAllByCondition(any(Pageable.class), any(ReadAuctionSearchCondition.class)))
                .willReturn(readAuctionsDto);

        // when & then
        mockMvc.perform(get("/auctions")
                       .contentType(MediaType.APPLICATION_JSON)
                       .queryParam("size", "10")
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
                                       parameterWithName("lastAuctionId").description("마지막으로 조회한 경매 ID").optional(),
                                       parameterWithName("size").description("페이지 크기").optional()
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
    void 지정한_아이디에_해당하는_경매를_삭제한다() throws Exception {
        // given
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        willDoNothing().given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders
                       .delete("/auctions/{auctionId}", 1L)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(status().isNoContent())
               .andDo(
                       restDocs.document(
                               requestHeaders(
                                       headerWithName("Authorization").description("회원 Bearer 인증 정보")
                               ),
                               pathParameters(
                                       parameterWithName("auctionId").description("삭제할 경매 ID")
                               )
                       )
               );
    }

    @Test
    void 경매_삭제시_지정한_아이디에_해당하는_경매가_없다면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        willThrow(new AuctionNotFoundException("지정한 아이디에 대한 경매를 찾을 수 없습니다."))
                .given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", invalidAuctionId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_삭제시_유효한_회원이_아니라면_404를_반환한다() throws Exception {
        // given
        final Long invalidAuctionId = -999L;
        final PrivateClaims privateClaims = new PrivateClaims(1L);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        willThrow(new UserNotFoundException("회원 정보를 찾을 수 없습니다."))
                .given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", invalidAuctionId)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
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
                2,
                "main2",
                "sub2",
                1L,
                "https://profile.com",
                "판매자",
                3.5d
        );
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final ReadAuctionsDto readAuctionsDto = new ReadAuctionsDto(List.of(auction2, auction1), true);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.readAllByUserId(anyLong(), any(Pageable.class))).willReturn(readAuctionsDto);

        // when & then
        mockMvc.perform(get("/auctions/mines")
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
                2,
                "main2",
                "sub2",
                1L,
                "https://profile.com",
                "판매자",
                3.5d
        );
        final PrivateClaims privateClaims = new PrivateClaims(1L);
        final ReadAuctionsDto readAuctionsDto = new ReadAuctionsDto(List.of(auction2, auction1), true);

        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(privateClaims));
        given(auctionService.readAllByBidderId(anyLong(), any(Pageable.class))).willReturn(readAuctionsDto);

        // when & then
        mockMvc.perform(get("/auctions/bids")
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
