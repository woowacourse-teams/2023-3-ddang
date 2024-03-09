package com.ddang.ddang.auction.presentation;

import static org.hamcrest.Matchers.containsString;
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

import com.ddang.ddang.auction.application.dto.CreateAuctionDto;
import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.configuration.DescendingSortPageableArgumentResolver;
import com.ddang.ddang.auction.presentation.dto.request.ReadAuctionSearchCondition;
import com.ddang.ddang.auction.presentation.fixture.AuctionControllerFixture;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptorService;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.image.infrastructure.local.exception.EmptyImageException;
import com.ddang.ddang.image.infrastructure.local.exception.StoreImageFailureException;
import com.ddang.ddang.image.infrastructure.local.exception.UnsupportedImageFileExtensionException;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("NonAsciiCharacters")
class AuctionControllerTest extends AuctionControllerFixture {

    TokenDecoder mockTokenDecoder;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockTokenDecoder = mock(TokenDecoder.class);

        final AuthenticationStore store = new AuthenticationStore();
        final AuthenticationInterceptor interceptor = new AuthenticationInterceptor(
                new AuthenticationInterceptorService(
                        blackListTokenService,
                        authenticationUserService,
                        mockTokenDecoder,
                        store
                )
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
        given(auctionService.create(any(CreateAuctionDto.class))).willReturn(경매_등록_결과_dto);
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));

        // when & then
        final ResultActions resultAuctions = mockMvc.perform(multipart("/auctions")
                                                            .file(유효한_경매_이미지_파일)
                                                            .file(유효한_경매_등록_request_multipartFile)
                                                            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                                                            .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
                                                    )
                                                    .andExpectAll(
                                                            status().isCreated(),
                                                            header().string(HttpHeaders.LOCATION, is("/auctions/1")),
                                                            jsonPath("$.id", is(경매_등록_결과_dto.id()), Long.class),
                                                            jsonPath("$.title", is(경매_등록_결과_dto.title())),
                                                            jsonPath("$.image", containsString(경매_이미지_상대_주소)),
                                                            jsonPath("$.auctionPrice", is(경매_등록_결과_dto.startPrice())),
                                                            jsonPath("$.status", is(경매_생성_시_경매_상태)),
                                                            jsonPath("$.auctioneerCount", is(경매_생성_시_경매_참여자_수))
                                                    );

        create_문서화(resultAuctions);
    }

    @Test
    void 경매_등록시_유효한_회원이_아니라면_404을_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효하지_않은_사용자_id_클레임));
        given(auctionService.create(any())).willThrow(new UserNotFoundException("지정한 판매자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(유효한_경매_이미지_파일)
                       .file(유효한_경매_등록_request_multipartFile)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_유효한_하위_카테고리가_아니라면_400을_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.create(any())).willThrow(
                new CategoryNotFoundException("지정한 카테고리를 찾을 수 없습니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(유효한_경매_이미지_파일)
                       .file(유효하지_않은_카테고리_경매_등록_request_multipartFile)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_유효한_세번째_지역이_아니라면_400을_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.create(any())).willThrow(
                new RegionNotFoundException("지정한 세 번째 지역이 없거나 세 번째 지역이 아닙니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(유효한_경매_이미지_파일)
                       .file(유효하지_않은_지역_경매_등록_request_multipartFile)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_이미지_파일의_데이터가_비어있다면_400을_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.create(any())).willThrow(
                new EmptyImageException("이미지 파일의 데이터가 비어 있습니다.")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(비어_있는_경매_이미지_파일)
                       .file(비어있는_경매_이미지_경매_등록_request_multipartFile)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_이미지_저장에_실패하면_500을_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.create(any())).willThrow(
                new StoreImageFailureException("이미지 저장에 실패했습니다.", null)
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(유효한_경매_이미지_파일)
                       .file(유효한_경매_등록_request_multipartFile)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isInternalServerError(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_등록시_지원하지_않는_확장자의_이미지면_400을_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.create(any())).willThrow(
                new UnsupportedImageFileExtensionException("지원하지 않는 확장자입니다. : ")
        );

        // when & then
        mockMvc.perform(multipart("/auctions")
                       .file(유효하지_않은_확장자_경매_이미지_파일)
                       .file(유효한_경매_등록_request_multipartFile)
                       .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_조회한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.readByAuctionId(anyLong())).willReturn(경매_조회_dto);
        given(chatRoomService.readChatInfoByAuctionId(anyLong(), any())).willReturn(쪽지방_dto);

        // when & then
        final ResultActions resultActions
                = mockMvc.perform(RestDocumentationRequestBuilders.get("/auctions/{auctionId}", 경매_조회_dto.id())
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
                         )
                         .andExpectAll(
                                 status().isOk(),
                                 jsonPath("$.auction.id", is(경매_조회_dto.id()), Long.class),
                                 jsonPath("$.auction.images").exists(),
                                 jsonPath("$.auction.title", is(경매_조회_dto.title())),
                                 jsonPath("$.auction.category.main", is(경매_조회_dto.mainCategory())),
                                 jsonPath("$.auction.category.sub", is(경매_조회_dto.subCategory())),
                                 jsonPath("$.auction.description", is(경매_조회_dto.description())),
                                 jsonPath("$.auction.startPrice", is(경매_조회_dto.startPrice())),
                                 jsonPath("$.auction.startPrice", is(경매_조회_dto.startPrice())),
                                 jsonPath("$.auction.lastBidPrice", is(경매_조회_dto.lastBidPrice())),
                                 jsonPath("$.auction.status", is(경매_조회_dto.auctionStatus().toString())),
                                 jsonPath("$.auction.bidUnit", is(경매_조회_dto.bidUnit())),
                                 jsonPath("$.auction.registerTime").exists(),
                                 jsonPath("$.auction.closingTime").exists(),
                                 jsonPath("$.auction.directRegions[0].first",
                                         is(경매_조회_dto.auctionRegions().get(0).firstRegionDto().regionName())),
                                 jsonPath("$.auction.directRegions[0].second",
                                         is(경매_조회_dto.auctionRegions().get(0).secondRegionDto().regionName())),
                                 jsonPath("$.auction.directRegions[0].third",
                                         is(경매_조회_dto.auctionRegions().get(0).thirdRegionDto().regionName())),
                                 jsonPath("$.auction.auctioneerCount", is(경매_조회_dto.auctioneerCount())),
                                 jsonPath("$.chat.id").exists(),
                                 jsonPath("$.chat.isChatParticipant", is(true)),
                                 jsonPath("$.isOwner", is(true)),
                                 jsonPath("$.isLastBidder", is(false)),
                                 jsonPath("$.seller.nickname", is(경매_조회_dto.sellerName())),
                                 jsonPath("$.seller.id", is(경매_조회_dto.sellerId()), Long.class),
                                 jsonPath("$.seller.image", containsString(프로필_이미지_상대_주소))
                         );

        read_문서화(resultActions);
    }

    @Test
    void 경매_조회시_지정한_아이디에_해당하는_경매가_없다면_404를_반환한다() throws Exception {
        // given
        given(auctionService.readByAuctionId(anyLong()))
                .willThrow(new AuctionNotFoundException("지정한 아이디에 대한 경매를 찾을 수 없습니다."));
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));

        // when & then
        mockMvc.perform(get("/auctions/{auctionId}", 존재하지_않는_경매_id)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 첫번째_페이지의_경매_목록을_조회한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        given(auctionService.readAllByCondition(any(Pageable.class), any(ReadAuctionSearchCondition.class)))
                .willReturn(경매_목록_조회_dto);

        // when & then
        final ResultActions resultActions = mockMvc.perform(get("/auctions")
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .queryParam("size", "10")
                                                           .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
                                                   )
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.auctions.[0].id", is(두번째_경매_조회_dto.id()),
                                                                   Long.class),
                                                           jsonPath("$.auctions.[0].title", is(두번째_경매_조회_dto.title())),
                                                           jsonPath("$.auctions.[0].image",
                                                                   containsString(경매_이미지_상대_주소)),
                                                           jsonPath("$.auctions.[0].auctionPrice",
                                                                   is(두번째_경매_조회_dto.startPrice())),
                                                           jsonPath("$.auctions.[0].status").exists(),
                                                           jsonPath("$.auctions.[0].auctioneerCount",
                                                                   is(두번째_경매_조회_dto.auctioneerCount())),
                                                           jsonPath("$.auctions.[1].id", is(첫번째_경매_조회_dto.id()),
                                                                   Long.class),
                                                           jsonPath("$.auctions.[1].title", is(첫번째_경매_조회_dto.title())),
                                                           jsonPath("$.auctions.[1].image",
                                                                   containsString(경매_이미지_상대_주소)),
                                                           jsonPath("$.auctions.[1].auctionPrice",
                                                                   is(첫번째_경매_조회_dto.startPrice())),
                                                           jsonPath("$.auctions.[1].status").exists(),
                                                           jsonPath("$.auctions.[1].auctioneerCount",
                                                                   is(첫번째_경매_조회_dto.auctioneerCount())),
                                                           jsonPath("$.isLast").exists()
                                                   );

        readAllByCondition_문서화(resultActions);
    }

    @Test
    void 지정한_아이디에_해당하는_경매를_삭제한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        willDoNothing().given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        final ResultActions resultActions
                = mockMvc.perform(RestDocumentationRequestBuilders.delete("/auctions/{auctionId}", 유효한_경매_id)
                                                                  .contentType(MediaType.APPLICATION_JSON)
                                                                  .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
                         )
                         .andExpectAll(status().isNoContent());

        delete_문서화(resultActions);
    }

    @Test
    void 경매_삭제시_지정한_아이디에_해당하는_경매가_없다면_404를_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        willThrow(new AuctionNotFoundException("지정한 아이디에 대한 경매를 찾을 수 없습니다."))
                .given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", 존재하지_않는_경매_id)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_삭제시_유효한_회원이_아니라면_404를_반환한다() throws Exception {
        // given
        given(mockTokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_사용자_id_클레임));
        willThrow(new UserNotFoundException("회원 정보를 찾을 수 없습니다."))
                .given(auctionService).deleteByAuctionId(anyLong(), anyLong());

        // when & then
        mockMvc.perform(delete("/auctions/{auctionId}", 존재하지_않는_경매_id)
                       .contentType(MediaType.APPLICATION_JSON)
                       .header(HttpHeaders.AUTHORIZATION, 유효한_액세스_토큰)
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

    private void read_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("사용자 Bearer 인증 정보")
                        ),
                        pathParameters(
                                parameterWithName("auctionId").description("조회하고자 하는 경매 ID")
                        ),
                        responseFields(
                                fieldWithPath("auction.id").type(JsonFieldType.NUMBER).description("경매 글 ID"),
                                fieldWithPath("auction.images").type(JsonFieldType.ARRAY).description("경매 이미지"),
                                fieldWithPath("auction.title").type(JsonFieldType.STRING).description("경매 글 제목"),
                                fieldWithPath("auction.category").type(JsonFieldType.OBJECT)
                                                                 .description("경매 카테고리"),
                                fieldWithPath("auction.category.main").type(JsonFieldType.STRING)
                                                                      .description("상위 카테고리"),
                                fieldWithPath("auction.category.sub").type(JsonFieldType.STRING)
                                                                     .description("하위 카테고리"),
                                fieldWithPath("auction.description").type(JsonFieldType.STRING)
                                                                    .description("경매 본문"),
                                fieldWithPath("auction.startPrice").type(JsonFieldType.NUMBER)
                                                                   .description("시작가"),
                                fieldWithPath("auction.lastBidPrice").description("마지막 입찰가"),
                                fieldWithPath("auction.status").description("경매 상태"),
                                fieldWithPath("auction.bidUnit").type(JsonFieldType.NUMBER).description("입찰 단위"),
                                fieldWithPath("auction.registerTime").type(JsonFieldType.STRING)
                                                                     .description("경매 등록시간"),
                                fieldWithPath("auction.closingTime").type(JsonFieldType.STRING)
                                                                    .description("경매 마감시간"),
                                fieldWithPath("auction.directRegions").type(JsonFieldType.ARRAY)
                                                                      .description("모든 직거래 지역"),
                                fieldWithPath("auction.directRegions.[]").type(JsonFieldType.ARRAY)
                                                                         .description("단일 직거래 지역"),
                                fieldWithPath("auction.directRegions.[].first").type(JsonFieldType.STRING)
                                                                               .description("첫 번째 직거래 지역"),
                                fieldWithPath("auction.directRegions.[].second").type(JsonFieldType.STRING)
                                                                                .description("두 번째 직거래 지역"),
                                fieldWithPath("auction.directRegions.[].third").type(JsonFieldType.STRING)
                                                                               .description("세 번째 직거래 지역"),
                                fieldWithPath("auction.auctioneerCount").type(JsonFieldType.NUMBER)
                                                                        .description("경매 참여자 수"),
                                fieldWithPath("seller").type(JsonFieldType.OBJECT).description("판매자 정보"),
                                fieldWithPath("seller.id").type(JsonFieldType.NUMBER).description("판매자 ID"),
                                fieldWithPath("seller.image").type(JsonFieldType.STRING)
                                                             .description("판매자 프로필 이미지 주소"),
                                fieldWithPath("seller.nickname").type(JsonFieldType.STRING)
                                                                .description("판매자 닉네임"),
                                fieldWithPath("seller.reliability").type(JsonFieldType.NUMBER)
                                                                   .description("판매자 신뢰도"),
                                fieldWithPath("chat.id").type(JsonFieldType.NUMBER).description("채팅방 ID"),
                                fieldWithPath("chat.isChatParticipant").type(JsonFieldType.BOOLEAN)
                                                                       .description(
                                                                               "로그인한 사용자가 채팅방 생성이 가능한 사용자인지에 대한 여부"),
                                fieldWithPath("isOwner").type(JsonFieldType.BOOLEAN)
                                                        .description("로그인한 사용자가 해당 경매 글을 작성한 사용자인지에 대한 여부"),
                                fieldWithPath("isLastBidder").type(JsonFieldType.BOOLEAN)
                                                             .description("로그인한 사용자가 해당 경매의 최고 입찰자인지에 대한 여부")
                        )
                )
        );
    }

    private void readAllByCondition_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
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
                                fieldWithPath("auctions.[]").type(JsonFieldType.ARRAY)
                                                            .description("조회한 단일 경매 정보"),
                                fieldWithPath("auctions.[].id").type(JsonFieldType.NUMBER).description("경매 ID"),
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
                                fieldWithPath("isLast").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
                        )
                )
        );
    }

    private void delete_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
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
}
