package com.ddang.ddang.questionandanswer.presentation;

import com.ddang.ddang.auction.application.exception.AuctionNotFoundException;
import com.ddang.ddang.auction.configuration.DescendingSortPageableArgumentResolver;
import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.questionandanswer.application.dto.CreateAnswerDto;
import com.ddang.ddang.questionandanswer.application.dto.CreateQuestionDto;
import com.ddang.ddang.questionandanswer.application.exception.AlreadyAnsweredException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidAnswererException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidAuctionToAskQuestionException;
import com.ddang.ddang.questionandanswer.application.exception.InvalidQuestionerException;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateAnswerRequest;
import com.ddang.ddang.questionandanswer.presentation.dto.request.CreateQuestionRequest;
import com.ddang.ddang.questionandanswer.presentation.fixture.QuestionAndAnswerControllerFixture;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class QuestionAndAnswerControllerTest extends QuestionAndAnswerControllerFixture {

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

        mockMvc = MockMvcBuilders.standaloneSetup(questionAndAnswerController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .addInterceptors(interceptor)
                                 .setCustomArgumentResolvers(resolver, new DescendingSortPageableArgumentResolver())
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 질문을_등록한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.create(any(CreateQuestionDto.class))).willReturn(생성된_질문_아이디);

        // when & then
        final ResultActions resultActions = mockMvc.perform(post("/questions")
                                                           .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                                                           .contentType(MediaType.APPLICATION_JSON)
                                                           .content(objectMapper.writeValueAsString(질문_등록_request))
                                                   )
                                                   .andExpectAll(
                                                           status().isCreated(),
                                                           header().string(HttpHeaders.LOCATION, is("/auctions/1"))
                                                   );

        createQuestion_문서화(resultActions);
    }

    @Test
    void 경매_아이디를_입력하지_않은_경우_질문시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(경매_아이디가_없는_질문_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_아이디를_양수가_아닌_경우_질문시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(경매_아이디가_음수인_질문_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @ParameterizedTest
    @MethodSource("provideQuestionRequestWithEmptyContent")
    void 질문을_입력하지_않은_경우_질문시_400을_반환한다(final CreateQuestionRequest conte질문_내용이_빈값인_질문_등록_request) throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(conte질문_내용이_빈값인_질문_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    private static Stream<CreateQuestionRequest> provideQuestionRequestWithEmptyContent() {
        return Stream.of(질문_내용이_null인_질문_등록_request, 질문_내용이_빈값인_질문_등록_request);
    }

    @Test
    void 존재하지_않은_사용자가_질문시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(존재하지_않는_사용자_ID_클레임));
        given(questionService.create(any(CreateQuestionDto.class)))
                .willThrow(new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(질문_등록_request))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 존재하지_않은_경매에_질문시_404를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.create(any(CreateQuestionDto.class)))
                .willThrow(new AuctionNotFoundException("해당 경매를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(존재하지_않는_경매에_대한_질문_등록_request))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 이미_종료된_경매에_질문시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.create(any(CreateQuestionDto.class)))
                .willThrow(new InvalidAuctionToAskQuestionException("이미 종료된 경매입니다"));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(종료된_경매에_대한_질문_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 삭제된_경매에_질문시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.create(any(CreateQuestionDto.class)))
                .willThrow(new InvalidAuctionToAskQuestionException("삭제된 경매입니다"));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(삭제된_경매에_대한_질문_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매자가_본인이_등록한_경매에_질문시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(questionService.create(any(CreateQuestionDto.class)))
                .willThrow(new InvalidQuestionerException("경매 등록자는 질문할 수 없습니다"));

        // when & then
        mockMvc.perform(post("/questions")
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(판매자가_본인_경매에_대한_질문_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 답변을_등록한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(answerService.create(any(CreateAnswerDto.class))).willReturn(생성된_답변_아이디);

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.post("/questions/{questionId}/answers", 질문_아이디)
                                                                .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                .content(objectMapper.writeValueAsString(답변_등록_request))
                       )
                       .andExpectAll(
                               status().isCreated(),
                               header().string(HttpHeaders.LOCATION, is("/auctions/1"))
                       );

        createAnswer_문서화(resultActions);
    }

    @Test
    void 경매_아이디를_입력하지_않은_경우_답변시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(경매_아이디가_없는_답변_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 경매_아이디가_양수가_아닌_경우_답변시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(양수가_아닌_경매_아이디에_대한_답변_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @ParameterizedTest
    @MethodSource("provideAnswerRequestWithEmptyContent")
    void 답변을_입력하지_않은_경우_답변시_400을_반환한다(final CreateAnswerRequest 답변_내용이_빈값인_답변_등록_request) throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(답변_내용이_빈값인_답변_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    private static Stream<CreateAnswerRequest> provideAnswerRequestWithEmptyContent() {
        return Stream.of(답변_내용이_null인_답변_등록_request, 답변_내용이_빈값인_답변_등록_request);
    }

    @Test
    void 존재하지_않은_사용자가_답변시_404을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(존재하지_않는_사용자_ID_클레임));
        given(answerService.create(any(CreateAnswerDto.class)))
                .willThrow(new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(답변_등록_request))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 존재하지_않은_질문에_답변시_404을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(answerService.create(any(CreateAnswerDto.class)))
                .willThrow(new AuctionNotFoundException("해당 질문을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 존재하지_않는_질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(답변_등록_request))
               )
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 판매자가_아닌_사용자가_질문에_답변시_400을_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(answerService.create(any(CreateAnswerDto.class)))
                .willThrow(new InvalidAnswererException("판매자만 답변할 수 있습니다."));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(판매자가_아닌_사용자가_질문에_대한_답변_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 이미_답변한_질문에_답변시_400을_반환한다() throws Exception {
        // givens
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(사용자_ID_클레임));
        given(answerService.create(any(CreateAnswerDto.class)))
                .willThrow(new AlreadyAnsweredException("이미 답변한 질문입니다."));

        // when & then
        mockMvc.perform(post("/questions/{questionId}/answers", 질문_아이디)
                       .header(HttpHeaders.AUTHORIZATION, 액세스_토큰_값)
                       .contentType(MediaType.APPLICATION_JSON)
                       .content(objectMapper.writeValueAsString(이미_답변한_질문에_대한_답변_등록_request))
               )
               .andExpectAll(
                       status().isBadRequest(),
                       jsonPath("$.message").exists()
               );
    }

    public void createQuestion_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        requestFields(
                                fieldWithPath("auctionId").description("질문할 경매 ID"),
                                fieldWithPath("content").description("질문 내용")
                        )
                )
        );
    }

    public void createAnswer_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        requestHeaders(
                                headerWithName("Authorization").description("회원 Bearer 인증 정보")
                        ),
                        pathParameters(
                                parameterWithName("questionId").description("답변할 질문 ID")
                        ),
                        requestFields(
                                fieldWithPath("auctionId").description("답변할 질문의 경매 ID"),
                                fieldWithPath("content").description("답변 내용")
                        )
                )
        );
    }
}
