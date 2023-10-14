package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.domain.repository.UserReliabilityRepository;
import com.ddang.ddang.user.infrastructure.persistence.fixture.UserReliabilityRepositoryImplFixture;
import org.junit.jupiter.api.BeforeEach;
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
class UserReliabilityRepositoryImplTest extends UserReliabilityRepositoryImplFixture {

    UserReliabilityRepository userReliabilityRepository;

    @BeforeEach
    void setUp(@Autowired final JpaUserReliabilityRepository jpaUserReliabilityRepository) {
        userReliabilityRepository = new UserReliabilityRepositoryImpl(jpaUserReliabilityRepository);
    }

    @Test
    void 사용자_신뢰도_정보를_저장한다() {
        // when
        final UserReliability actual = userReliabilityRepository.save(저장하기_전_사용자_신뢰도_엔티티);

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
