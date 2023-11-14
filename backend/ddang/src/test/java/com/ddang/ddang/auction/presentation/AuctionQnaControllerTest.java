package com.ddang.ddang.auction.presentation;

import com.ddang.ddang.auction.infrastructure.persistence.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.presentation.fixture.AuctionQuestionControllerFixture;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class AuctionQnaControllerTest extends AuctionQuestionControllerFixture {

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

        mockMvc = MockMvcBuilders.standaloneSetup(auctionQnaController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver)
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 경매_아이디를_통해_질문과_답변을_모두_조회한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.readAllByAuctionId(anyLong(), anyLong())).willReturn(질문과_답변_정보들_dto);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(get("/auctions/{auctionId}/questions", 조회할_경매_아이디)
                               .header(HttpHeaders.AUTHORIZATION, 엑세스_토큰_값)
                               .contentType(MediaType.APPLICATION_JSON)
                       )
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.qnas.[0].question.id", is(질문_정보_dto1.id()), Long.class),
                               jsonPath("$.qnas.[0].question.writer.id", is(질문자_정보_dto.id()), Long.class),
                               jsonPath("$.qnas.[0].question.writer.name", is(질문자_정보_dto.name())),
                               jsonPath("$.qnas.[0].question.writer.image").exists(),
                               jsonPath("$.qnas.[0].question.createdTime").exists(),
                               jsonPath("$.qnas.[0].question.content", is(질문_정보_dto1.content())),
                               jsonPath("$.qnas.[0].question.isQuestioner", is(질문_정보_dto1.isQuestioner()), Boolean.class),
                               jsonPath("$.qnas.[0].answer.id", is(답변_정보_dto1.id()), Long.class),
                               jsonPath("$.qnas.[0].answer.writer.id", is(판매자_정보_dto.id()), Long.class),
                               jsonPath("$.qnas.[0].answer.writer.name", is(판매자_정보_dto.name())),
                               jsonPath("$.qnas.[0].answer.writer.image").exists(),
                               jsonPath("$.qnas.[0].answer.createdTime").exists(),
                               jsonPath("$.qnas.[0].answer.content", is(답변_정보_dto1.content())),
                               jsonPath("$.qnas.[1].question.id", is(질문_정보_dto2.id()), Long.class),
                               jsonPath("$.qnas.[1].question.writer.id", is(질문자_정보_dto.id()), Long.class),
                               jsonPath("$.qnas.[1].question.writer.name", is(질문자_정보_dto.name())),
                               jsonPath("$.qnas.[1].question.writer.image").exists(),
                               jsonPath("$.qnas.[1].question.createdTime").exists(),
                               jsonPath("$.qnas.[1].question.content", is(질문_정보_dto2.content())),
                               jsonPath("$.qnas.[1].question.isQuestioner", is(질문_정보_dto2.isQuestioner()), Boolean.class),
                               jsonPath("$.qnas.[1].answer.id", is(답변_정보_dto2.id()), Long.class),
                               jsonPath("$.qnas.[1].answer.writer.id", is(판매자_정보_dto.id()), Long.class),
                               jsonPath("$.qnas.[1].answer.writer.name", is(판매자_정보_dto.name())),
                               jsonPath("$.qnas.[1].answer.writer.image").exists(),
                               jsonPath("$.qnas.[1].answer.createdTime").exists(),
                               jsonPath("$.qnas.[1].answer.content", is(답변_정보_dto2.content()))
                       );

        readAllByAuctionId_문서화(resultActions);
    }

    @Test
    void 존재하지_않는_경매_아이디를_통해_질문과_답변을_모두_조회할시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.readAllByAuctionId(anyLong(), anyLong()))
                .willThrow(new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/auctions/{auctionId}/questions", 조회할_경매_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 엑세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    private void readAllByAuctionId_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("qnas.[]").type(JsonFieldType.ARRAY)
                                                        .description("모든 경매 질문과 답변 목록"),
                                fieldWithPath("qnas.[].question").type(JsonFieldType.OBJECT)
                                                                 .description("질문 정보 JSON"),
                                fieldWithPath("qnas.[].question.id").type(JsonFieldType.NUMBER)
                                                                    .description("경매 질문 글 ID"),
                                fieldWithPath("qnas.[].question.writer").type(JsonFieldType.OBJECT)
                                                                        .description("질문자 정보 JSON"),
                                fieldWithPath("qnas.[].question.writer.id").type(JsonFieldType.NUMBER)
                                                                           .description("질문자의 ID"),
                                fieldWithPath("qnas.[].question.writer.name").type(JsonFieldType.STRING)
                                                                             .description("질문자의 이름"),
                                fieldWithPath("qnas.[].question.writer.image").type(JsonFieldType.STRING)
                                                                              .description("질문자의 프로필 이미지 URL"),
                                fieldWithPath("qnas.[].question.createdTime").type(JsonFieldType.STRING)
                                                                             .description("질문 등록 시간"),
                                fieldWithPath("qnas.[].question.content").type(JsonFieldType.STRING)
                                                                         .description("질문 내용"),
                                fieldWithPath("qnas.[].question.isQuestioner").type(JsonFieldType.BOOLEAN)
                                                                              .description("질문 작성자 여부 확인"),
                                fieldWithPath("qnas.[].answer").type(JsonFieldType.OBJECT)
                                                               .description("답변 정보 JSON"),
                                fieldWithPath("qnas.[].answer.id").type(JsonFieldType.NUMBER)
                                                                  .description("경매 답변 글 ID"),
                                fieldWithPath("qnas.[].answer.writer").type(JsonFieldType.OBJECT)
                                                                      .description("답변자 정보 JSON"),
                                fieldWithPath("qnas.[].answer.writer.id").type(JsonFieldType.NUMBER)
                                                                         .description("답변자의 ID"),
                                fieldWithPath("qnas.[].answer.writer.name").type(JsonFieldType.STRING)
                                                                           .description("답변자의 이름"),
                                fieldWithPath("qnas.[].answer.writer.image").type(JsonFieldType.STRING)
                                                                            .description("답변자의 프로필 이미지 URL"),
                                fieldWithPath("qnas.[].answer.createdTime").type(JsonFieldType.STRING)
                                                                           .description("답변 등록 시간"),
                                fieldWithPath("qnas.[].answer.content").type(JsonFieldType.STRING)
                                                                       .description("답변 내용")
                        )
                )
        );
    }
}
