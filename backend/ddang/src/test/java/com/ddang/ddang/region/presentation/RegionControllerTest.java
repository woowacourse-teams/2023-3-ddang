package com.ddang.ddang.region.presentation;

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

import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import com.ddang.ddang.region.application.dto.ReadRegionDto;
import com.ddang.ddang.region.application.exception.RegionNotFoundException;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@SuppressWarnings("NonAsciiCharacters")
class RegionControllerTest extends CommonControllerSliceTest {

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

        final ReadRegionDto first1 = new ReadRegionDto(1L, "서울특별시");
        final ReadRegionDto first2 = new ReadRegionDto(2L, "부산광역시");

        given(regionService.readAllFirst()).willReturn(List.of(first1, first2));

        // when & then
        mockMvc.perform(get("/regions")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.[0].id", is(first1.id()), Long.class),
                       jsonPath("$.[0].name", is(first1.name())),
                       jsonPath("$.[1].id", is(first2.id()), Long.class),
                       jsonPath("$.[1].name", is(first2.name()))
               )
               .andDo(
                        restDocs.document(
                                responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("모든 첫 번째 직거래 지역"),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("첫 번째 직거래 지역 ID"),
                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("첫 번째 직거래 지역 이름")
                                )
                        )
                );
    }

    @Test
    void 첫번째_지역이_없는_경우_첫번째_지역_조회시_404를_반환한다() throws Exception {
        // given
        final RegionNotFoundException regionNotFoundException = new RegionNotFoundException("등록된 지역이 없습니다.");
        given(regionService.readAllFirst()).willThrow(regionNotFoundException);

        // when & then
        mockMvc.perform(get("/regions")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(regionNotFoundException.getMessage()))
               );
    }

    @Test
    void 첫번째_지역에_해당하는_모든_두번째_지역을_조회한다() throws Exception {
        // given
        final ReadRegionDto first = new ReadRegionDto(1L, "서울특별시");
        final ReadRegionDto second1 = new ReadRegionDto(2L, "강남구");
        final ReadRegionDto second2 = new ReadRegionDto(3L, "강동구");

        given(regionService.readAllSecondByFirstRegionId(first.id())).willReturn(List.of(second1, second2));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/regions/{firstId}", first.id())
                                                        .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.[0].id", is(second1.id()), Long.class),
                       jsonPath("$.[0].name", is(second1.name())),
                       jsonPath("$.[1].id", is(second2.id()), Long.class),
                       jsonPath("$.[1].name", is(second2.name()))
               )
               .andDo(
                       restDocs.document(
                               pathParameters(
                                       parameterWithName("firstId").description("첫 번째 지역 ID")
                               ),
                               responseFields(
                                       fieldWithPath("[]").type(JsonFieldType.ARRAY).description("첫 번째 지역에 해당하는 모든 두 번째 직거래 지역"),
                                       fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("두 번째 직거래 지역 ID"),
                                       fieldWithPath("[].name").type(JsonFieldType.STRING).description("두 번째 직거래 지역 이름")
                               )
                       )
               );
    }

    @Test
    void 지정한_첫번째_지역에_해당하는_두번째_지역이_없는_경우_두번째_지역_조회시_404를_반환한다() throws Exception {
        // given
        final ReadRegionDto first = new ReadRegionDto(1L, "first");

        final RegionNotFoundException regionNotFoundException =
                new RegionNotFoundException("지정한 첫 번째 지역에 해당하는 두 번째 지역이 없습니다.");

        given(regionService.readAllSecondByFirstRegionId(first.id())).willThrow(regionNotFoundException);

        // when & then
        mockMvc.perform(get("/regions/{firstId}", first.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(regionNotFoundException.getMessage()))
               );
    }

    @Test
    void 두번째_지역에_해당하는_모든_세번째_지역을_조회한다() throws Exception {
        // given
        final ReadRegionDto first = new ReadRegionDto(1L, "서울특별시");
        final ReadRegionDto second = new ReadRegionDto(2L, "강남구");
        final ReadRegionDto third1 = new ReadRegionDto(3L, "개포1동");
        final ReadRegionDto third2 = new ReadRegionDto(4L, "개포2동");

        given(regionService.readAllThirdByFirstAndSecondRegionId(first.id(), second.id()))
                .willReturn(List.of(third1, third2));

        // when & then
        mockMvc.perform(RestDocumentationRequestBuilders.get("/regions/{firstId}/{secondId}", first.id(), second.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.[0].id", is(third1.id()), Long.class),
                       jsonPath("$.[0].name", is(third1.name())),
                       jsonPath("$.[1].id", is(third2.id()), Long.class),
                       jsonPath("$.[1].name", is(third2.name()))
               )
               .andDo(
                        restDocs.document(
                                pathParameters(
                                    parameterWithName("firstId").description("첫 번째 지역 ID"),
                                    parameterWithName("secondId").description("두 번째 지역 ID")
                                ),
                                responseFields(
                                        fieldWithPath("[]").type(JsonFieldType.ARRAY).description("두 번째 지역에 해당하는 모든 세 번째 직거래 지역"),
                                        fieldWithPath("[].id").type(JsonFieldType.NUMBER).description("세 번째 직거래 지역 ID"),
                                        fieldWithPath("[].name").type(JsonFieldType.STRING).description("세 번째 직거래 지역 이름")
                                )
                        )
                );
    }

    @Test
    void 지정한_첫번째와_두번째_지역에_해당하는_세번째_지역이_없는_경우_세번째_지역_조회시_404를_반환한다() throws Exception {
        // given
        final ReadRegionDto first = new ReadRegionDto(1L, "first");
        final ReadRegionDto second = new ReadRegionDto(2L, "second");

        final RegionNotFoundException regionNotFoundException =
                new RegionNotFoundException("지정한 첫 번째와 두 번째 지역에 해당하는 세 번째 지역이 없습니다.");

        given(regionService.readAllThirdByFirstAndSecondRegionId(first.id(), second.id()))
                .willThrow(regionNotFoundException);

        // when & then
        mockMvc.perform(get("/regions/{firstId}/{secondId}", first.id(), second.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(regionNotFoundException.getMessage()))
               );
    }
}
