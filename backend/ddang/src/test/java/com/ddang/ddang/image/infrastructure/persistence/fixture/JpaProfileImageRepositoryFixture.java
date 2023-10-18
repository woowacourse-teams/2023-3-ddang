package com.ddang.ddang.image.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.infrastructure.persistence.JpaProfileImageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaProfileImageRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaProfileImageRepository profileImageRepository;

    protected String 업로드_이미지_파일명 = "uploadName";
    protected String 저장된_이미지_파일명 = "storeName";
    protected String 존재하는_프로필_이미지_이름 = "프로필이미지.png";
    protected String 존재하지_않는_프로필_이미지_이름 = "invalid.png";

    @BeforeEach
    void setUp() {
        final ProfileImage 프로필_이미지 = new ProfileImage("프로필이미지.png", 존재하는_프로필_이미지_이름);

        profileImageRepository.save(프로필_이미지);

        em.flush();
        em.clear();
    }
}
