package com.ddang.ddang.user.domain;

import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.user.domain.fixture.ReliabilityFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ReliabilityTest extends ReliabilityFixture {

    @Test
    void 신뢰도_점수_평균을_계산한다() {
        // when
        평가_대상.updateReliability(평가_대상이_받은_평가_목록);

        // then
        assertThat(평가_대상.getReliability().getValue()).isEqualTo(평가_대상의_신뢰도_점수);
    }

    @Test
    void 신뢰도_기록이_없다면_신뢰도_점수는_null이다() {
        // given
        final List<Review> targetReviews = Collections.emptyList();

        // when
        평가_대상.updateReliability(targetReviews);

        // then
        assertThat(평가_대상.getReliability().getValue()).isNull();
    }
}
