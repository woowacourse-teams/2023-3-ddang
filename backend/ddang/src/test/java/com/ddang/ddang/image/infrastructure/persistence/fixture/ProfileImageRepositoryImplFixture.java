package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class ProfileImageRepositoryImplFixture {

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    protected String 존재하는_프로필_이미지_이름 = "프로필이미지.png";
    protected String 존재하지_않는_프로필_이미지_이름 = "invalid.png";

    @BeforeEach
    void fixtureSetUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필이미지.png", 존재하는_프로필_이미지_이름);

        profileImageRepository.save(프로필_이미지);
    }
}
