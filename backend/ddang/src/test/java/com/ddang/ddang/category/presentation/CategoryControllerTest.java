package com.ddang.ddang.category.presentation;

import com.ddang.ddang.category.application.CategoryService;
import com.ddang.ddang.category.application.dto.ReadCategoryDto;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {CategoryController.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CategoryControllerTest {

    @MockBean
    CategoryService categoryService;

    @Autowired
    CategoryController categoryController;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController)
                                 .setControllerAdvice(new GlobalExceptionHandler())
                                 .build();
    }

    @Test
    void 모든_메인_카테고리를_조회한다() throws Exception {
        // given
        final ReadCategoryDto main1 = new ReadCategoryDto(1L, "main1");
        final ReadCategoryDto main2 = new ReadCategoryDto(2L, "main2");

        given(categoryService.readAllMain()).willReturn(List.of(main1, main2));

        // when & then
        mockMvc.perform(get("/categories")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.categories.[0].id", is(main1.id()), Long.class),
                       jsonPath("$.categories.[0].name", is(main1.name())),
                       jsonPath("$.categories.[1].id", is(main2.id()), Long.class),
                       jsonPath("$.categories.[1].name", is(main2.name()))
               );
    }

    @Test
    void 메인_카테고리가_없는_경우_메인_카테고리_조회시_404를_반환한다() throws Exception {
        // given
        CategoryNotFoundException categoryNotFoundException = new CategoryNotFoundException("등록된 메인 카테고리가 없습니다.");
        given(categoryService.readAllMain()).willThrow(categoryNotFoundException);

        // when & then
        mockMvc.perform(get("/categories")
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(categoryNotFoundException.getMessage()))
               );
    }

    @Test
    void 메인_카테고리에_해당하는_모든_서브_카테고리를_조회한다() throws Exception {
        // given
        final ReadCategoryDto main = new ReadCategoryDto(1L, "main");
        final ReadCategoryDto sub1 = new ReadCategoryDto(2L, "sub1");
        final ReadCategoryDto sub2 = new ReadCategoryDto(3L, "sub2");

        given(categoryService.readAllSubByMainId(main.id())).willReturn(List.of(sub1, sub2));

        // when & then
        mockMvc.perform(get("/categories/{mainId}", main.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isOk(),
                       jsonPath("$.categories.[0].id", is(sub1.id()), Long.class),
                       jsonPath("$.categories.[0].name", is(sub1.name())),
                       jsonPath("$.categories.[1].id", is(sub2.id()), Long.class),
                       jsonPath("$.categories.[1].name", is(sub2.name()))
               );
    }

    @Test
    void 지정한_메인_카테고리에_해당_서브_카테고리가_없는_경우_서브_카테고리_조회시_404를_반환한다() throws Exception {
        // given
        final ReadCategoryDto main = new ReadCategoryDto(1L, "main");

        CategoryNotFoundException categoryNotFoundException =
                new CategoryNotFoundException("지정한 메인 카테고리에 해당 서브 카테고리가 없습니다.");

        given(categoryService.readAllSubByMainId(main.id())).willThrow(categoryNotFoundException);

        // when & then
        mockMvc.perform(get("/categories/{mainId}", main.id())
                       .contentType(MediaType.APPLICATION_JSON))
               .andExpectAll(
                       status().isNotFound(),
                       jsonPath("$.message", is(categoryNotFoundException.getMessage()))
               );
    }
}
