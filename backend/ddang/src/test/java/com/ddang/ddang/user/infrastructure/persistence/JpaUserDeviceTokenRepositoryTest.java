package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.UserDeviceToken;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaUserDeviceTokenRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Autowired
    JpaUserDeviceTokenRepository userDeviceTokenRepository;

    @Test
    void 주어진_사용자_아이디에_해당하는_기기토큰을_조회한다() {
        // given
        final String deviceToken = "token1234";

        final User user = User.builder()
                              .name("사용자")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        userRepository.save(user);

        final UserDeviceToken expect = new UserDeviceToken(user, deviceToken);
        userDeviceTokenRepository.save(expect);

        em.flush();
        em.clear();

        // when
        final Optional<UserDeviceToken> actual = userDeviceTokenRepository.findByUserId(user.getId());

        // then
        assertThat(actual).contains(expect);
    }
}
