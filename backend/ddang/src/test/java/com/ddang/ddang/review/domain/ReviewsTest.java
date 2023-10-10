package com.ddang.ddang.review.domain;

import com.ddang.ddang.review.domain.fixture.ReviewsFixture;
import com.ddang.ddang.user.domain.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class ReviewsTest extends ReviewsFixture {

    @Test
    void 리뷰들의_점수를_모두_더해서_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(사용자1의_평가1, 사용자1의_평가2));

        // when
        final double actual = reviews.addAllReviewScore();

        // then
        assertThat(actual).isEqualTo(평가1_점수 + 평가2_점수);
    }

    @Test
    void 리뷰들의_개수를_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(사용자1의_평가1, 사용자1의_평가2));

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
        final Reviews reviews = new Reviews(List.of(사용자1의_평가1, 사용자1의_평가2));

        // when
        final boolean actual = reviews.isEmpty();

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 평가_대상자_목록을_중복_없이_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(사용자1의_평가1, 사용자1의_평가2, 사용자2의_평가1, 사용자2의_평가2));

        // when
        final Set<User> actual = reviews.findReviewTargets();

        // then
        assertThat(actual).containsExactlyInAnyOrder(사용자1, 사용자2);
    }

    @Test
    void 평가_대상_사용자가_받은_평가_목록을_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(사용자1의_평가1, 사용자1의_평가2, 사용자2의_평가1, 사용자2의_평가2));

        // when
        final List<Review> actual = reviews.findReviewsByTarget(사용자1);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(2);
            softAssertions.assertThat(actual.get(0)).isEqualTo(사용자1의_평가1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(사용자1의_평가2);
        });
    }

    @Test
    void 평가_목록_중_가장_마지막_평가의_아이디를_반환한다() {
        // given
        final Reviews reviews = new Reviews(List.of(사용자1의_평가1, 사용자1의_평가2, 사용자2의_평가1, 사용자2의_평가2));

        // when
        final Long actual = reviews.findLastReviewId();

        // then
        assertThat(actual).isEqualTo(사용자2의_평가2.getId());
    }
}
