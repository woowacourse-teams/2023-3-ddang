package com.ddang.ddang.category.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.ddang.ddang.category.application.dto.ReadCategoryDto;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.application.fixture.CategoryServiceFixture;
import com.ddang.ddang.configuration.IsolateDatabase;
import java.util.List;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CategoryServiceTest extends CategoryServiceFixture {

    @Autowired
    CategoryService categoryService;

    @Test
    void 모든_메인_카테고리를_조회한다() {
        // when
        final List<ReadCategoryDto> actual = categoryService.readAllMain();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(가구_카테고리.getId());
            softAssertions.assertThat(actual.get(0).name()).isEqualTo(가구_카테고리.getName());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(전자기기_카테고리.getId());
            softAssertions.assertThat(actual.get(1).name()).isEqualTo(전자기기_카테고리.getName());
        });
    }

    @Test
    void 메인_카테고리에_해당하는_모든_서브_카테고리를_조회한다() {
        // when
        final List<ReadCategoryDto> actual = categoryService.readAllSubByMainId(가구_카테고리.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0).id()).isEqualTo(가구_하위_의자_카테고리.getId());
            softAssertions.assertThat(actual.get(0).name()).isEqualTo(가구_하위_의자_카테고리.getName());
            softAssertions.assertThat(actual.get(1).id()).isEqualTo(가구_하위_책상_카테고리.getId());
            softAssertions.assertThat(actual.get(1).name()).isEqualTo(가구_하위_책상_카테고리.getName());
        });
    }

    @Test
    void 지정한_메인_카테고리에_해당하는_서브_카테고리가_없는_경우_서브_카테고리_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> categoryService.readAllSubByMainId(전자기기_카테고리.getId()))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("지정한 메인 카테고리에 해당하는 서브 카테고리가 없습니다.");
    }
}
