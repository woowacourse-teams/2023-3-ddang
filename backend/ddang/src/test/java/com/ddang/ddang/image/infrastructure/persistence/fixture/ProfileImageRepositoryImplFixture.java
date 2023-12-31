package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class ProfileImageRepositoryImplFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    protected ProfileImage 프로필_이미지;

    @BeforeEach
    void fixtureSetUp() {
        프로필_이미지 = new ProfileImage("프로필이미지.png", "프로필이미지.png");

        profileImageRepository.save(프로필_이미지);

        em.flush();
        em.clear();
    }
}
