package com.ddang.ddang.review.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.review.application.dto.CreateReviewDto;
import com.ddang.ddang.review.application.exception.AlreadyReviewException;
import com.ddang.ddang.review.application.exception.ReviewNotFoundException;
import com.ddang.ddang.review.presentation.fixture.ReviewControllerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class ReviewControllerTest extends ReviewControllerFixture {

    TokenDecoder tokenDecoder;
    MockMvc mockMvc;

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

        mockMvc = MockMvcBuilders.standaloneSetup(reviewController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 평가를_등록한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_작성자_비공개_클레임));
        given(reviewService.create(any(CreateReviewDto.class))).willReturn(생성된_평가_아이디);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.post("/reviews")
                                                                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(사용자_평가_등록_요청))
                ).andExpectAll(
                        status().isCreated(),
                        header().string(HttpHeaders.LOCATION, is("/reviews/" + 생성된_평가_아이디))
                );

        create_문서화(resultActions);
    }

    @Test
    void 이미_평가를_등록했다면_400를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_작성자_비공개_클레임));
        given(reviewService.create(any(CreateReviewDto.class))).willThrow(new AlreadyReviewException("이미 평가하였습니다."));

        // when & then
        mockMvc.perform(post("/reviews")
                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(중복된_평가_등록_요청))
        ).andExpectAll(
                status().isBadRequest(),
                jsonPath("$.message").exists()
        );
    }

    @Test
    void 주어진_사용자가_받은_평가_목록을_최신순으로_조회한다() throws Exception {
        // given
        given(reviewService.readAllByTargetId(anyLong()))
                .willReturn(List.of(구매자가_판매자2에게_받은_평가, 구매자가_판매자1에게_받은_평가));

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.get("/reviews/users/{userId}", String.valueOf(구매자.id()))
                                                                .contentType(MediaType.APPLICATION_JSON)
                       )
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.[0].id", is(구매자가_판매자2에게_받은_평가.id()), Long.class),
                               jsonPath("$.[0].writer.id", is(구매자가_판매자2에게_받은_평가.writer().id()), Long.class),
                               jsonPath("$.[0].writer.name", is(구매자가_판매자2에게_받은_평가.writer().name())),
                               jsonPath("$.[0].content", is(구매자가_판매자2에게_받은_평가.content())),
                               jsonPath("$.[0].score", is(구매자가_판매자2에게_받은_평가.score())),
                               jsonPath("$.[1].id", is(구매자가_판매자1에게_받은_평가.id()), Long.class),
                               jsonPath("$.[1].writer.id", is(구매자가_판매자1에게_받은_평가.writer().id()), Long.class),
                               jsonPath("$.[1].writer.name", is(구매자가_판매자1에게_받은_평가.writer().name())),
                               jsonPath("$.[1].content", is(구매자가_판매자1에게_받은_평가.content())),
                               jsonPath("$.[1].score", is(구매자가_판매자1에게_받은_평가.score()))
                       );

        readAllReviewsOfTargetUser_문서화(resultActions);
    }

    @Test
    void 지정한_평가_아이디에_해당하는_평가를_조회한다() throws Exception {
        given(reviewService.readByReviewId(anyLong()))
                .willReturn(구매자가_판매자1에게_받은_평가_내용);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.get("/reviews/{reviewId}", 구매자가_판매자1에게_받은_평가_아이디)
                                                                .contentType(MediaType.APPLICATION_JSON)
                       )
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("score", is(구매자가_판매자1에게_받은_평가_내용.score()), Double.class),
                               jsonPath("content", is(구매자가_판매자1에게_받은_평가_내용.content()))
                       );

        read_문서화(resultActions);
    }

    @Test
    void 지정한_평가_아이디에_해당하는_평가가_존재하지_않는다면_404가_발생한다() throws Exception {
        given(reviewService.readByReviewId(anyLong()))
                .willThrow(new ReviewNotFoundException("해당 평가를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/reviews/{reviewId}", 구매자가_판매자1에게_받은_평가_아이디)
                                                        .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("message").exists()
               );
    }

    private void create_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName(HttpHeaders.AUTHORIZATION).description("회원 Bearer 인증 정보")
                        ),
                        requestFields(
                                fieldWithPath("auctionId").type(JsonFieldType.NUMBER)
                                                          .description("거래한 경매 ID"),
                                fieldWithPath("targetId").type(JsonFieldType.NUMBER)
                                                         .description("평가 대상 ID"),
                                fieldWithPath("score").type(JsonFieldType.NUMBER)
                                                      .description("평가 점수"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                                        .description("평가 내용")
                        )
                )
        );
    }

    private void readAllReviewsOfTargetUser_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("userId").description("평가 목록 조회 대상 유저의 아이디")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                   .description("조회 대상 사용자가 받은 모든 평가 목록"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                                      .description("사용자 평가 ID"),
                                fieldWithPath("[].writer.id").type(JsonFieldType.NUMBER)
                                                             .description("평가를 작성한 사용자의 ID"),
                                fieldWithPath("[].writer.name").type(JsonFieldType.STRING)
                                                               .description("평가를 작성한 사용자의 이름"),
                                fieldWithPath("[].writer.profileImage").type(JsonFieldType.STRING)
                                                                       .description("평가를 작성한 사용자의 프로필 이미지 url"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING)
                                                           .description("평가 내용"),
                                fieldWithPath("[].score").type(JsonFieldType.NUMBER)
                                                         .description("평가 점수"),
                                fieldWithPath("[].createdTime").type(JsonFieldType.STRING)
                                                               .description("평가 작성 시간")
                        )
                )
        );
    }

    private void read_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("reviewId").description("평가와_관련된_경매_아이디")
                        ),
                        responseFields(
                                fieldWithPath("score").type(JsonFieldType.NUMBER)
                                                      .description("평가 점수"),
                                fieldWithPath("content").type(JsonFieldType.STRING)
                                                        .description("평가 내용")
                        )
                )
        );
    }
}
