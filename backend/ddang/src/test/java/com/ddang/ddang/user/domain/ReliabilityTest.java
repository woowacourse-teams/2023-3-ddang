package com.ddang.ddang.user.domain;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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
}
