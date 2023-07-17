package com.ddang.ddang.category.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

class CategoryTest {

    @Test
    void 카테고리_연관_관계를_세팅한다() {
        // given
        Category main = new Category("main");
        Category sub = new Category("sub");

        // when
        main.addSubCategory(sub);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(main.getSubCategories())
                          .hasSize(1);
            softAssertions.assertThat(sub.getMainCategory())
                          .isNotNull();
        });
    }
}
