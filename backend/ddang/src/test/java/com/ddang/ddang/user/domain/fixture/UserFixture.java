package com.ddang.ddang.user.domain.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class UserFixture {

    protected double 새로운_신뢰도_점수 = 3.0d;
    protected User 평가_대상;
    protected User 프로필_이미지가_있는_회원;
    protected User 프로필_이미지가_없는_회원;
    protected String 프로필_이미지_파일_이름 = "profile.png";
    protected Reliability 새로운_신뢰도;

    @BeforeEach
    void setUp() {
        평가_대상 = User.builder()
                    .name("평가 대상")
                    .profileImage(new ProfileImage("profile.png", 프로필_이미지_파일_이름))
                    .reliability(new Reliability(0.0d))
                    .oauthId("12345")
                    .build();
        프로필_이미지가_있는_회원 = User.builder()
                             .name("평가 대상")
                             .profileImage(new ProfileImage("profile.png", 프로필_이미지_파일_이름))
                             .reliability(new Reliability(0.0d))
                             .oauthId("12345")
                             .build();
        프로필_이미지가_없는_회원 = User.builder()
                             .name("평가 대상")
                             .reliability(new Reliability(0.0d))
                             .oauthId("12345")
                             .build();

        새로운_신뢰도 = new Reliability(새로운_신뢰도_점수);
    }
}
