package com.ddang.ddang.user.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReliabilityUpdateHistoryTest {

    @Test
    void 빈_생성자로_생성하면_마지막으로_적용된_평가_아이디가_0이다() {
        // given
        final Long expect = 0L;

        // when
        final ReliabilityUpdateHistory actual = new ReliabilityUpdateHistory();

        // then
        assertThat(actual.getLastAppliedReviewId()).isEqualTo(expect);
    }

    @Test
    void 생성자에_마직막으로_적용된_평가_아이디를_지정할_수_있다() {
        // given
        final Long lastAppliedReviewId = 2L;

        // when
        final ReliabilityUpdateHistory actual = new ReliabilityUpdateHistory(2L);

        // then
        assertThat(actual.getLastAppliedReviewId()).isEqualTo(lastAppliedReviewId);
    }
}
