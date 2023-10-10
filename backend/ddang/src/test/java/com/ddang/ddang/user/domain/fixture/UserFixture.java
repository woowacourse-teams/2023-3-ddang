package com.ddang.ddang.user.domain.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.review.domain.Score;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class UserFixture {

    private double 평가1_점수 = 5.0d;
    private double 평가2_점수 = 1.0d;
    private User 평가_작성자1 = User.builder()
                               .name("평가 작성자1")
                               .profileImage(new ProfileImage("profile.png", "profile.png"))
                               .reliability(new Reliability(4.7d))
                               .oauthId("12346")
                               .build();
    private User 평가_작성자2 = User.builder()
                               .name("평가 작성자2")
                               .profileImage(new ProfileImage("profile.png", "profile.png"))
                               .reliability(new Reliability(4.7d))
                               .oauthId("12347")
                               .build();

    protected User 평가_대상 = User.builder()
                               .name("평가 대상")
                               .profileImage(new ProfileImage("profile.png", "profile.png"))
                               .reliability(new Reliability(0.0d))
                               .oauthId("12345")
                               .build();
    private Review 평가1 = Review.builder()
                               .writer(평가_작성자1)
                               .target(평가_대상)
                               .content("친절하다")
                               .score(new Score(평가1_점수))
                               .build();
    private Review 평가2 = Review.builder()
                               .writer(평가_작성자2)
                               .target(평가_대상)
                               .content("별로다")
                               .score(new Score(평가2_점수))
                               .build();

    protected List<Review> 평가_대상이_받은_평가_목록 = List.of(평가1, 평가2);
    protected double 평가_대상의_신뢰도_점수 = (평가1_점수 + 평가2_점수) / 2;
}
