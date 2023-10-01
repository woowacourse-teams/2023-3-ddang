package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.fixture.JpaUserRepositoryFixture;
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
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaUserRepositoryTest extends JpaUserRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 사용자를_저장한다() {
        // given
        final User user = User.builder()
                              .name("새로운 사용자")
                              .profileImage(프로필_이미지)
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();

        // when
        userRepository.save(user);

        // then
        em.flush();
        em.clear();

        assertThat(user.getId()).isPositive();
    }

    @Test
    void 존재하는_oauthId를_전달하면_해당_회원을_Optional로_감싸_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByOauthIdAndDeletedIsFalse(사용자.getOauthId());

        // then
        assertThat(actual).contains(사용자);
    }

    @Test
    void 존재하지_않는_oauthId를_전달하면_해당_회원을_빈_Optional로_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByOauthIdAndDeletedIsFalse(존재하지_않는_oauth_아이디);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 회원가입과_탈퇴하지_않은_회원_id를_전달하면_해당_회원을_Optional로_감싸_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(사용자.getId());

        // then
        assertThat(actual).contains(사용자);
    }

    @Test
    void 회원탈퇴한_회원의_id를_전달하면_빈_Optional을_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(탈퇴한_사용자.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 없는_id를_전달하면_빈_Optional을_반환한다() {
        // when
        final Optional<User> actual = userRepository.findByIdAndDeletedIsFalse(존재하지_않는_사용자_아이디);

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 회원탈퇴한_회원의_id를_전달하면_참을_반환한다() {
        // when
        final boolean actual = userRepository.existsByIdAndDeletedIsTrue(탈퇴한_사용자.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 회원탈퇴하지_않거나_회원가입하지_않은_회원의_id를_전달하면_거짓을_반환한다() {
        // when
        final boolean actual = userRepository.existsByIdAndDeletedIsTrue(사용자.getId());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 이름이_아직_없다면_거짓을_반환한다() {
        // when
        final boolean actual = userRepository.existsByNameEndingWith(존재하지_않는_사용자_이름);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 이름이_있다면_참을_반환한다() {
        // when
        final boolean actual = userRepository.existsByNameEndingWith(존재하는_사용자_이름);

        // then
        assertThat(actual).isTrue();
    }
}
