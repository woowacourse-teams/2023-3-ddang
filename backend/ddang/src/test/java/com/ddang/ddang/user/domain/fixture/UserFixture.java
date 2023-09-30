package com.ddang.ddang.user.domain.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.review.domain.Review;
import com.ddang.ddang.user.domain.User;

@SuppressWarnings("NonAsciiCharacters")
public class UserFixture {

    private double 평가1_점수 = 5.0d;
    private double 평가2_점수 = 1.0d;
    private User 평가_작성자1 = User.builder()
                               .name("평가 작성자1")
                               .profileImage(new ProfileImage("profile.png", "profile.png"))
                               .reliability(4.7d)
                               .oauthId("12346")
                               .build();
    private User 평가_작성자2 = User.builder()
                               .name("평가 작성자2")
                               .profileImage(new ProfileImage("profile.png", "profile.png"))
                               .reliability(4.7d)
                               .oauthId("12347")
                               .build();

    protected User 평가_대상 = User.builder()
                               .name("평가 대상")
                               .profileImage(new ProfileImage("profile.png", "profile.png"))
                               .reliability(null)
                               .oauthId("12345")
                               .build();
    protected Review 평가1 = Review.builder()
                                 .writer(평가_작성자1)
                                 .target(평가_대상)
                                 .content("친절하다")
                                 .score(평가1_점수)
                                 .build();
    protected Review 평가2 = Review.builder()
                                 .writer(평가_작성자2)
                                 .target(평가_대상)
                                 .content("별로다")
                                 .score(평가2_점수)
                                 .build();
}
