package com.ddang.ddang.user.infrastructure.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.Optional;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaUserRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 존재하는_oauthId를_전달하면_해당_회원을_Optional로_감싸_반환한다() {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        em.flush();
        em.clear();

        // when
        final Optional<User> actual = userRepository.findByOauthId(user.getOauthId());

        // then
        assertThat(actual).isPresent();
    }

    @Test
    void 존재하지_않는_oauthId를_전달하면_해당_회원을_빈_Optional로_반환한다() {
        // given
        final String invalidOauthId = "invalidOauthId";

        // when
        final Optional<User> actual = userRepository.findByOauthId(invalidOauthId);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 회원가입과_탈퇴하지_않은_회원_id를_전달하면_해당_회원을_Optional로_감싸_반환한다() {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        userRepository.save(user);

        em.flush();
        em.clear();

        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(user.getId());

        // then
        assertThat(actual).isPresent();
    }

    @Test
    void 회원탈퇴한_회원의_id를_전달하면_빈_Optional을_반환한다() {
        // given
        final User user = User.builder()
                              .name("회원")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        user.withdrawal();
        userRepository.save(user);

        em.flush();
        em.clear();

        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(user.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 없는_id를_전달하면_빈_Optional을_반환한다() {
        // given
        final Long invalidUserId = -999L;

        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(invalidUserId);

        // then
        assertThat(actual).isEmpty();
    }
}
