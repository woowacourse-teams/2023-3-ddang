package com.ddang.ddang.user.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class JpaUserRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private JpaUserRepository userRepository;

    protected ProfileImage 프로필_이미지 = new ProfileImage("upload.png", "store.png");
    protected String 존재하지_않는_oauth_아이디 = "invalidOauthId";
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected String 존재하지_않는_사용자_이름 = "새로운 이름";
    protected String 존재하는_사용자_이름;
    protected User 사용자;
    protected User 탈퇴한_사용자;

    @BeforeEach
    void setUp() {
        final ProfileImage 사용자_프로필_이미지 = new ProfileImage("upload.png", "store.png");
        사용자 = User.builder()
                  .name("사용자")
                  .profileImage(사용자_프로필_이미지)
                  .reliability(new Reliability(4.7f))
                  .oauthId("12345")
                  .build();

        탈퇴한_사용자 = User.builder()
                      .name("탈퇴한 사용자")
                      .profileImage(사용자_프로필_이미지)
                      .reliability(new Reliability(4.7f))
                      .oauthId("12345")
                      .build();
        탈퇴한_사용자.withdrawal();

        userRepository.saveAll(List.of(사용자, 탈퇴한_사용자));

        존재하는_사용자_이름 = 사용자.getName();

        em.flush();
        em.clear();
    }
}
