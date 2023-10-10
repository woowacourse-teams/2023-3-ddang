package com.ddang.ddang.review.domain;

import com.ddang.ddang.review.domain.fixture.ReviewsFixture;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewsTest extends ReviewsFixture {

    @Test
    void 리뷰들의_점수를_모두_더해서_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(평가1, 평가2));

        // when
        final double actual = reviews.addAllReviewScore();

        // then
        assertThat(actual).isEqualTo(평가1_점수 + 평가2_점수);
    }

    @Test
    void 리뷰들의_개수를_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(평가1, 평가2));

        // when
        final int actual = reviews.size();

        // then
        assertThat(actual).isEqualTo(2);
    }

    @Test
    void 리뷰가_없다면_참을_반환한다() {
        // given
        final Reviews reviews = new Reviews(Collections.emptyList());

        // when
        final boolean actual = reviews.isEmpty();

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 리뷰가_존재한다면_거짓을_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(평가1, 평가2));

        // when
        final boolean actual = reviews.isEmpty();

        // then
        assertThat(actual).isFalse();
    }
}
