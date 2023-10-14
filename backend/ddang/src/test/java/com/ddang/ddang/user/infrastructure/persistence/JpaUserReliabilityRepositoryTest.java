package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.infrastructure.persistence.fixture.JpaUserReliabilityRepositoryFixture;
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
class JpaUserReliabilityRepositoryTest extends JpaUserReliabilityRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaUserReliabilityRepository userReliabilityRepository;

    @Test
    void 사용자_신뢰도_정보를_저장한다() {
        // when
        UserReliability actual = userReliabilityRepository.save(저장하기_전_사용자_신뢰도_엔티티);

        em.flush();
        em.clear();

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 주어진_사용자_아이디에_해당하는_사용자_신뢰도_정보를_반환한다() {
        // when
        final Optional<UserReliability> actual = userReliabilityRepository.findByUserId(사용자.getId());

        // then
        assertThat(actual).contains(사용자_신뢰도_정보);
    }
}
