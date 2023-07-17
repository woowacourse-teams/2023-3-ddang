package com.ddang.ddang.category.application;

import com.ddang.ddang.category.application.dto.ReadCategoryDto;
import com.ddang.ddang.category.application.exception.CategoryNotFoundException;
import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.JpaCategoryRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CategoryServiceTest {

    @Autowired
    CategoryService categoryService;

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Test
    void 모든_메인_카테고리를_조회한다() {
        // given
        final Category main1 = new Category("main1");
        final Category main2 = new Category("main2");
        final Category sub = new Category("sub");

        main1.addSubCategory(sub);

        categoryRepository.save(main1);
        categoryRepository.save(main2);

        // when
        final List<ReadCategoryDto> actual = categoryService.readAllMain();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 메인_카테고리가_없는_경우_메인_카테고리_조회시_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> categoryService.readAllMain())
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("등록된 메인 카테고리가 없습니다.");
    }

    @Test
    void 메인_카테고리에_해당하는_모든_서브_카테고리를_조회한다() {
        // given
        final Category main = new Category("main");
        final Category sub1 = new Category("sub1");
        final Category sub2 = new Category("sub2");

        main.addSubCategory(sub1);
        main.addSubCategory(sub2);

        categoryRepository.save(main);

        // when
        final List<ReadCategoryDto> actual = categoryService.readAllSubByMainId(main.getId());

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    void 지정한_메인_카테고리에_해당_서브_카테고리가_없는_경우_서브_카테고리_조회시_예외가_발생한다() {
        // given
        final Category main = new Category("main");

        categoryRepository.save(main);

        // when & then
        assertThatThrownBy(() -> categoryService.readAllSubByMainId(main.getId()))
                .isInstanceOf(CategoryNotFoundException.class)
                .hasMessage("지정한 메인 카테고리에 해당 서브 카테고리가 없습니다.");
    }
}
