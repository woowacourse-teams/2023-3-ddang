package com.ddang.ddang.category.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.category.domain.Category;
import com.ddang.ddang.category.infrastructure.persistence.fixture.JpaCategoryRepositoryFixture;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(QuerydslConfiguration.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaCategoryRepositoryTest extends JpaCategoryRepositoryFixture {

    @Autowired
    JpaCategoryRepository categoryRepository;

    @Test
    void 모든_메인_카테고리를_조회한다() {
        // when
        final List<Category> actual = categoryRepository.findMainAllByMainCategoryIsNull();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual).contains(가구_카테고리);
            softAssertions.assertThat(actual).contains(전자기기_카테고리);
        });
    }

    @Test
    void 메인_카테고리에_해당하는_모든_서브_카테고리를_조회한다() {
        // when
        final List<Category> actual = categoryRepository.findSubAllByMainCategoryId(가구_카테고리.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(가구_서브_의자_카테고리);
            softAssertions.assertThat(actual.get(1)).isEqualTo(가구_서브_책상_카테고리);
        });
    }

    @Test
    void 서브_카테고리를_조회한다() {
        // when
        final Optional<Category> actual = categoryRepository.findSubCategoryById(가구_서브_의자_카테고리.getId());

        // then
        assertThat(actual).contains(가구_서브_의자_카테고리);
    }

    @Test
    void 서브_카테고리가_아닌_카테고리의_아이디를_전달하면_빈_Optional을_반환한다() {
        // when
        final Optional<Category> actual = categoryRepository.findSubCategoryById(가구_카테고리.getId());

        // then
        assertThat(actual).isEmpty();
    }
}
