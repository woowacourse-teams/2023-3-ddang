package com.ddang.ddang.authentication.application.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class AuthenticationUserServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    protected User 사용자;
    protected User 탈퇴한_사용자;

    @BeforeEach
    void setUp() {
        사용자 = User.builder()
                  .name("kakao12345")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(0.0d))
                  .oauthId("12345")
                  .build();

        탈퇴한_사용자 = User.builder()
                      .name("kakao12346")
                      .profileImage(new ProfileImage("upload.png", "store.png"))
                      .reliability(new Reliability(0.0d))
                      .oauthId("12346")
                      .build();

        userRepository.save(사용자);
        userRepository.save(탈퇴한_사용자);

        탈퇴한_사용자.withdrawal();
    }
}
