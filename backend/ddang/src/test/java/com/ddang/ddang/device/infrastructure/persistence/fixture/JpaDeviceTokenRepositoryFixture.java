package com.ddang.ddang.device.infrastructure.persistence.fixture;

import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class JpaDeviceTokenRepositoryFixture {

    @PersistenceContext
    private EntityManager em;

    private UserRepository userRepository;

    @Autowired
    private JpaDeviceTokenRepository jpaDeviceTokenRepository;

    protected User 사용자;
    protected DeviceToken 사용자의_디바이스_토큰;

    @BeforeEach
    void setUp(@Autowired final JpaUserRepository jpaUserRepository) {
        userRepository = new UserRepositoryImpl(jpaUserRepository);

        사용자 = User.builder()
                  .name("사용자")
                  .profileImage(new ProfileImage("upload.png", "store.png"))
                  .reliability(new Reliability(4.7d))
                  .oauthId("12345")
                  .build();

        사용자의_디바이스_토큰 = new DeviceToken(사용자, "deviceToken");

        userRepository.save(사용자);
        jpaDeviceTokenRepository.save(사용자의_디바이스_토큰);

        em.flush();
        em.clear();
    }
}
