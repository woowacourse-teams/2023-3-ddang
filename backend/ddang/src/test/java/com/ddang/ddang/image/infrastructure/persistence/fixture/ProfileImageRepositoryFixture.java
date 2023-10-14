package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class ProfileImageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaProfileImageRepository jpaProfileImageRepository;

    protected ProfileImage 프로필_이미지;

    @BeforeEach
    void setUpFixture() {
        프로필_이미지 = new ProfileImage("프로필이미지.png", "프로필이미지.png");

        jpaProfileImageRepository.save(프로필_이미지);

        em.flush();
        em.clear();
    }
}
