package com.ddang.ddang.user.presentation.util;

import com.ddang.ddang.user.domain.Reliability;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReliabilityProcessorTest {

    @Test
    void 신뢰도가_초기신뢰도이면_null을_반환한다() {
        // given
        final double initialReliability = Reliability.INITIAL_RELIABILITY.getValue();

        // when
        final Float actual = ReliabilityProcessor.process(initialReliability);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 신뢰도가_초기신뢰도가_아니면_그_값을_Float_타입으로_변환한여_반환한다() {
        // given
        final double reliability = 3.5d;

        // when
        final Float actual = ReliabilityProcessor.process(reliability);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isInstanceOf(Float.class);
            softAssertions.assertThat(actual).isEqualTo((float) reliability);
        });
    }
}
