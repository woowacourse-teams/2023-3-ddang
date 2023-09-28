package com.ddang.ddang.category.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class CategoryTest {

    @Test
    void 카테고리_연관_관계를_세팅한다() {
        // given
        final Category main = new Category("main");
        final Category sub = new Category("sub");

        // when
        main.addSubCategory(sub);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(main.getSubCategories()).hasSize(1);
            softAssertions.assertThat(main.getSubCategories().get(0).getName()).isEqualTo(sub.getName());
            softAssertions.assertThat(sub.getMainCategory().getName()).isEqualTo(main.getName());
        });
    }
}
