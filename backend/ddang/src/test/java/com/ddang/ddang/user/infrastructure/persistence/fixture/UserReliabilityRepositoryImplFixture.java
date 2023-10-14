package com.ddang.ddang.user.infrastructure.persistence.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.domain.repository.UserReliabilityRepository;
import com.ddang.ddang.user.domain.repository.UserRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserReliabilityRepository;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import com.ddang.ddang.user.infrastructure.persistence.UserReliabilityRepositoryImpl;
import com.ddang.ddang.user.infrastructure.persistence.UserRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class UserReliabilityRepositoryImplFixture {

    private UserRepository userRepository;
    private UserReliabilityRepository userReliabilityRepository;

    protected User 사용자;
    protected UserReliability 저장하기_전_사용자_신뢰도_엔티티;
    protected UserReliability 사용자_신뢰도_정보;

    @BeforeEach
    void setUp(
            @Autowired JpaUserRepository jpaUserRepository,
            @Autowired JpaUserReliabilityRepository jpaUserReliabilityRepository
    ) {
        userRepository = new UserRepositoryImpl(jpaUserRepository);
        userReliabilityRepository = new UserReliabilityRepositoryImpl(jpaUserReliabilityRepository);

        사용자 = User.builder()
                  .name("사용자")
                  .profileImage(new ProfileImage("uplad.png", "store.png"))
                  .reliability(Reliability.INITIAL_RELIABILITY)
                  .oauthId("12345")
                  .build();
        userRepository.save(사용자);

        저장하기_전_사용자_신뢰도_엔티티 = new UserReliability(사용자);

        사용자_신뢰도_정보 = new UserReliability(사용자);
        userReliabilityRepository.save(사용자_신뢰도_정보);
    }
}
