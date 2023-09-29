package com.ddang.ddang.region.presentation;

import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import com.ddang.ddang.region.presentation.fixture.RegionControllerFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("NonAsciiCharacters")
class RegionControllerTest extends RegionControllerFixture {

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(regionController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                                 .alwaysDo(print())
                                 .alwaysDo(restDocs)
                                 .build();
    }

    @Test
    void 모든_첫번째_지역을_조회한다() throws Exception {
        // given
        given(regionService.readAllFirst()).willReturn(List.of(서울특별시, 부산광역시));

        // when & then
        final ResultActions resultActions = mockMvc.perform(get("/regions")
                                                           .contentType(MediaType.APPLICATION_JSON))
                                                   .andExpectAll(
                                                           status().isOk(),
                                                           jsonPath("$.[0].id", is(서울특별시.id()), Long.class),
                                                           jsonPath("$.[0].name", is(서울특별시.name())),
                                                           jsonPath("$.[1].id", is(부산광역시.id()), Long.class),
                                                           jsonPath("$.[1].name", is(부산광역시.name()))
                                                   );

        readAllFirst_문서화(resultActions);
    }

    @Test
    void 첫번째_지역이_없는_경우_첫번째_지역_조회시_404를_반환한다() throws Exception {
        // given
        given(regionService.readAllFirst()).willThrow(new RegionNotFoundException("등록된 지역이 없습니다."));

        // when & then
        mockMvc.perform(get("/regions")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 첫번째_지역에_해당하는_모든_두번째_지역을_조회한다() throws Exception {
        // given
        given(regionService.readAllSecondByFirstRegionId(서울특별시.id())).willReturn(List.of(서울특별시_강남구, 서울특별시_강동구));

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.get("/regions/{firstId}", 서울특별시.id())
                                                                .contentType(MediaType.APPLICATION_JSON))
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.[0].id", is(서울특별시_강남구.id()), Long.class),
                               jsonPath("$.[0].name", is(서울특별시_강남구.name())),
                               jsonPath("$.[1].id", is(서울특별시_강동구.id()), Long.class),
                               jsonPath("$.[1].name", is(서울특별시_강동구.name()))
                       );

        readAllSecond_문서화(resultActions);
    }

    @Test
    void 지정한_첫번째_지역에_해당하는_두번째_지역이_없는_경우_두번째_지역_조회시_404를_반환한다() throws Exception {
        // given
        given(regionService.readAllSecondByFirstRegionId(두번째_지역이_없는_첫번째_지역.id()))
                .willThrow(new RegionNotFoundException("지정한 첫 번째 지역에 해당하는 두 번째 지역이 없습니다."));

        // when & then
        mockMvc.perform(get("/regions/{firstId}", 두번째_지역이_없는_첫번째_지역.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    @Test
    void 두번째_지역에_해당하는_모든_세번째_지역을_조회한다() throws Exception {
        // given
        given(regionService.readAllThirdByFirstAndSecondRegionId(서울특별시.id(), 서울특별시_강남구.id()))
                .willReturn(List.of(서울특별시_강남구_개포1동, 서울특별시_강남구_개포2동));

        // when & then
        final ResultActions resultActions =
                mockMvc.perform(RestDocumentationRequestBuilders.get("/regions/{firstId}/{secondId}",
                                                                        서울특별시.id(),
                                                                        서울특별시_강남구.id())
                                                                .contentType(MediaType.APPLICATION_JSON))
                       .andExpectAll(
                               status().isOk(),
                               jsonPath("$.[0].id", is(서울특별시_강남구_개포1동.id()), Long.class),
                               jsonPath("$.[0].name", is(서울특별시_강남구_개포1동.name())),
                               jsonPath("$.[1].id", is(서울특별시_강남구_개포2동.id()), Long.class),
                               jsonPath("$.[1].name", is(서울특별시_강남구_개포2동.name()))
                       );

        readAllThird_문서화(resultActions);
    }

    @Test
    void 지정한_첫번째와_두번째_지역에_해당하는_세번째_지역이_없는_경우_세번째_지역_조회시_404를_반환한다() throws Exception {
        // given
        given(regionService.readAllThirdByFirstAndSecondRegionId(서울특별시.id(), 세번째_지역이_없는_두번째_지역.id()))
                .willThrow(new RegionNotFoundException("지정한 첫 번째와 두 번째 지역에 해당하는 세 번째 지역이 없습니다."));

        // when & then
        mockMvc.perform(get("/regions/{firstId}/{secondId}", 서울특별시.id(), 세번째_지역이_없는_두번째_지역.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message").exists()
               );
    }

    private void readAllFirst_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                   .description("모든 첫 번째 직거래 지역"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                                      .description("첫 번째 직거래 지역 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                                        .description("첫 번째 직거래 지역 이름")
                        )
                )
        );
    }

    private void readAllSecond_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("firstId").description("첫 번째 지역 ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                   .description("첫 번째 지역에 해당하는 모든 두 번째 직거래 지역"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                                      .description("두 번째 직거래 지역 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                                        .description("두 번째 직거래 지역 이름")
                        )
                )
        );
    }

    private void readAllThird_문서화(final ResultActions resultActions) throws Exception {
        resultActions.andDo(
                restDocs.document(
                        pathParameters(
                                parameterWithName("firstId")
                                        .description("첫 번째 지역 ID"),
                                parameterWithName("secondId")
                                        .description("두 번째 지역 ID")
                        ),
                        responseFields(
                                fieldWithPath("[]").type(JsonFieldType.ARRAY)
                                                   .description("두 번째 지역에 해당하는 모든 세 번째 직거래 지역"),
                                fieldWithPath("[].id").type(JsonFieldType.NUMBER)
                                                      .description("세 번째 직거래 지역 ID"),
                                fieldWithPath("[].name").type(JsonFieldType.STRING)
                                                        .description("세 번째 직거래 지역 이름")
                        )
                )
        );
    }
}
