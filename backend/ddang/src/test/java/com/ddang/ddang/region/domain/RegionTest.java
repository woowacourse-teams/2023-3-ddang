package com.ddang.ddang.region.domain;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class RegionTest {

    @Test
    void 첫번째_지역과_두번째_지역의_연관관계를_세팅한다() {
        // given
        final Region first = new Region("서울특별시");
        final Region second = new Region("강남구");

        // when
        first.addSecondRegion(second);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(first.getSecondRegions()).hasSize(1);
            softAssertions.assertThat(first.getSecondRegions().get(0).getName()).isEqualTo(second.getName());
            softAssertions.assertThat(second.getFirstRegion().getName()).isEqualTo(first.getName());
        });
    }

    @Test
    void 두번째_지역과_세번째_지역의_연관관계를_세팅한다() {
        // given
        final Region first = new Region("서울특별시");
        final Region second = new Region("강남구");
        final Region third = new Region("역삼동");

        first.addSecondRegion(second);

        // when
        second.addThirdRegion(third);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(second.getThirdRegions()).hasSize(1);
            softAssertions.assertThat(second.getThirdRegions().get(0).getName()).isEqualTo(third.getName());
            softAssertions.assertThat(third.getSecondRegion().getName()).isEqualTo(second.getName());
            softAssertions.assertThat(third.getFirstRegion().getName()).isEqualTo(first.getName());
        });
    }
}
