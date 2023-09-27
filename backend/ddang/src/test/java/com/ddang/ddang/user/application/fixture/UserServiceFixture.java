package com.ddang.ddang.user.application.fixture;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@IsolateDatabase
public class UserServiceFixture {

    @Autowired
    private JpaUserRepository userRepository;

    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected MockMultipartFile 프로필_이미지 = new MockMultipartFile(
            "updateImage.png",
            "updateImage.png",
            MediaType.IMAGE_PNG.toString(),
            new byte[]{1}
    );

    protected User 사용자;

    @BeforeEach
    void setUp() {
        사용자 = User.builder()
                  .name("사용자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(4.7d)
                  .oauthId("12345")
                  .build();

        userRepository.save(사용자);

    }
}
