package com.ddang.ddang.review.domain.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Score;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
public class ReviewsFixture {

    protected double 평가1_점수 = 1.5d;
    protected double 평가2_점수 = 2.5d;
    protected User 사용자1;
    protected User 사용자2;
    protected Review 사용자1의_평가1;
    protected Review 사용자1의_평가2;
    protected Review 사용자2의_평가1;
    protected Review 사용자2의_평가2;

    @BeforeEach
    void setUp() {
        사용자1 = User.builder()
                   .name("사용자1")
                   .profileImage(new ProfileImage("uplad.png", "store.png"))
                   .reliability(Reliability.INITIAL_RELIABILITY)
                   .oauthId("12345")
                   .build();
        사용자2 = User.builder()
                   .name("사용자2")
                   .profileImage(new ProfileImage("uplad.png", "store.png"))
                   .reliability(Reliability.INITIAL_RELIABILITY)
                   .oauthId("12346")
                   .build();
        ReflectionTestUtils.setField(사용자1, "id", 1L);
        ReflectionTestUtils.setField(사용자2, "id", 2L);

        사용자1의_평가1 = Review.builder()
                          .target(사용자1)
                          .score(new Score(평가1_점수))
                          .build();
        ReflectionTestUtils.setField(사용자1의_평가1, "id", 1L);
        사용자1의_평가2 = Review.builder()
                          .target(사용자1)
                          .score(new Score(평가2_점수))
                          .build();
        ReflectionTestUtils.setField(사용자1의_평가1, "id", 2L);
        사용자2의_평가1 = Review.builder()
                          .target(사용자2)
                          .score(new Score(평가1_점수))
                          .build();
        ReflectionTestUtils.setField(사용자2의_평가1, "id", 3L);
        사용자2의_평가2 = Review.builder()
                          .target(사용자2)
                          .score(new Score(평가2_점수))
                          .build();
        ReflectionTestUtils.setField(사용자2의_평가2, "id", 4L);
    }
}
