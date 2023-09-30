package com.ddang.ddang.review.presentation;

import com.ddang.ddang.authentication.configuration.AuthenticationInterceptor;
import com.ddang.ddang.authentication.configuration.AuthenticationPrincipalArgumentResolver;
import com.ddang.ddang.authentication.domain.TokenDecoder;
import com.ddang.ddang.authentication.domain.TokenType;
import com.ddang.ddang.authentication.domain.dto.AuthenticationStore;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.review.application.dto.CreateReviewDto;
import com.ddang.ddang.review.application.exception.AlreadyReviewException;
import com.ddang.ddang.review.presentation.fixture.ReviewControllerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        mockMvc.perform(post("/reviews")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(사용자_평가_등록_요청))
        ).andExpectAll(
                status().isCreated(),
                header().string(HttpHeaders.LOCATION, is("/reviews/" + 생성된_평가_아이디))
        );
    }

    @Test
    void 이미_평가를_등록했다면_400를_반환한다() throws Exception {
        // given
        given(tokenDecoder.decode(eq(TokenType.ACCESS), anyString())).willReturn(Optional.of(유효한_작성자_비공개_클레임));
        given(reviewService.create(any(CreateReviewDto.class))).willThrow(new AlreadyReviewException("이미 평가하였습니다."));

        // when & then
        mockMvc.perform(post("/reviews")
                .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
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
        given(reviewService.readAllByTargetId(anyLong())).willReturn(List.of(구매자가_판매자2에게_받은_평가, 구매자가_판매자1에게_받은_평가));

        // when & then
        mockMvc.perform(get("/reviews")
                       .queryParam("userId", String.valueOf(구매자.id()))
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
    }
}
