package com.ddang.ddang.review.domain.fixture;

import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Score;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class ReviewsFixture {

    protected double 평가1_점수 = 1.5d;
    protected double 평가2_점수 = 2.5d;
    protected Review 평가1;
    protected Review 평가2;

    @BeforeEach
    void setUp() {
        평가1 = Review.builder()
                    .score(new Score(평가1_점수))
                    .build();
        평가2 = Review.builder()
                    .score(new Score(평가2_점수))
                    .build();
    }
}
