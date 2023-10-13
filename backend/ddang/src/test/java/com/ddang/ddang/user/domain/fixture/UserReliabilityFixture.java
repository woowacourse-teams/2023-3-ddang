package com.ddang.ddang.user.domain.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Reviews;
import com.ddang.ddang.review.domain.Score;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

import java.util.Collections;
import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserReliabilityFixture {

    private double 새로운_평가1_점수 = 5.0d;
    private double 새로운_평가2_점수 = 1.0d;
    private double 새로운_평가_반영_전의_평가_대상의_신뢰도_점수 = 4.0d;
    protected int 새로운_평가_반영_전의_평가_대상의_신뢰도에_반영된_평가_개수 = 3;
    protected int 새로운_평가_반영_이후의_평가_대상의_신뢰도에_반영된_평가_개수 = 새로운_평가_반영_전의_평가_대상의_신뢰도에_반영된_평가_개수 + 2;
    protected double 새로운_평가_반영_이후의_평가_대상의_신뢰도_점수 =
            (새로운_평가_반영_전의_평가_대상의_신뢰도_점수 * 새로운_평가_반영_전의_평가_대상의_신뢰도에_반영된_평가_개수 +
                    새로운_평가1_점수 + 새로운_평가2_점수) / 새로운_평가_반영_이후의_평가_대상의_신뢰도에_반영된_평가_개수;
    protected User 기존에_평가_3개_받은_평가_대상;
    protected Reviews 평가_대상이_받은_새로운_평가_목록;
    protected Reviews 비어있는_평가_목록;
    protected Reliability 기존에_평가3개가_적용되어있는_신뢰도;
    protected Reliability 새로운_평가가_반영된_신뢰도;

    @BeforeEach
    void setUp() {
        final User 평가_작성자1 = User.builder()
                                 .name("평가 작성자1")
                                 .profileImage(new ProfileImage("profile.png", "profile.png"))
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("12346")
                                 .build();
        final User 평가_작성자2 = User.builder()
                                 .name("평가 작성자2")
                                 .profileImage(new ProfileImage("profile.png", "profile.png"))
                                 .reliability(new Reliability(4.7d))
                                 .oauthId("12347")
                                 .build();


        기존에_평가3개가_적용되어있는_신뢰도 = new Reliability(새로운_평가_반영_전의_평가_대상의_신뢰도_점수);

        기존에_평가_3개_받은_평가_대상 = User.builder()
                                 .name("평가 대상")
                                 .profileImage(new ProfileImage("profile.png", "profile.png"))
                                 .reliability(기존에_평가3개가_적용되어있는_신뢰도)
                                 .oauthId("12345")
                                 .build();

        final Review 새로운_평가1 = Review.builder()
                                     .writer(평가_작성자1)
                                     .target(기존에_평가_3개_받은_평가_대상)
                                     .content("친절하다")
                                     .score(new Score(새로운_평가1_점수))
                                     .build();
        final Review 새로운_평가2 = Review.builder()
                                     .writer(평가_작성자2)
                                     .target(기존에_평가_3개_받은_평가_대상)
                                     .content("별로다")
                                     .score(new Score(새로운_평가2_점수))
                                     .build();

        평가_대상이_받은_새로운_평가_목록 = new Reviews(List.of(새로운_평가1, 새로운_평가2));

        비어있는_평가_목록 = new Reviews(Collections.emptyList());

        새로운_평가가_반영된_신뢰도 = new Reliability(새로운_평가_반영_이후의_평가_대상의_신뢰도_점수);
    }
}
