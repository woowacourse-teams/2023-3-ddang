package com.ddang.ddang.user.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReliabilityTest {

    @Test
    void 신뢰도에_반영된_평가의_개수가_주어지면_신뢰도에_반영된_평가_점수들의_합을_반환한다() {
        // given
        final double reliabilityValue = 4.0d;
        final Reliability reliability = new Reliability(reliabilityValue);
        final int appliedReviewCount = 3;

        final double expect = reliabilityValue * appliedReviewCount;

        // when
        final double actual = reliability.calculateReviewScoreSum(appliedReviewCount);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 신뢰도가_지정한_수치보다_낮으면_true를_반환한다() {
        // given
        final Reliability reliability = new Reliability(4.0d);

        // when
        final boolean actual = reliability.isLessThan(5.0d);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 신뢰도가_지정한_수치보다_높으면_false를_반환한다() {
        // given
        final Reliability reliability = new Reliability(4.0d);

        // when
        final boolean actual = reliability.isLessThan(3.0d);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void double_타입의_신뢰도를_float_타입으로_변환한다() {
        // given
        final Reliability reliability = new Reliability(4.0d);

        // when
        final float actual = reliability.toFloat();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isInstanceOf(Float.class);
            softAssertions.assertThat(actual).isEqualTo(4.0f);
        });
    }
}
