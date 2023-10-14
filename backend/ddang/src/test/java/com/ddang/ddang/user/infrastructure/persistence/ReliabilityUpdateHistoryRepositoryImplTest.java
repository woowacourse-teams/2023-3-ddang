package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import com.ddang.ddang.user.domain.repository.ReliabilityUpdateHistoryRepository;
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
class ReliabilityUpdateHistoryRepositoryImplTest {

    ReliabilityUpdateHistoryRepository reliabilityUpdateHistoryRepository;

    @BeforeEach
    void setUp(@Autowired JpaReliabilityUpdateHistoryRepository jpaReliabilityUpdateHistoryRepository) {
        reliabilityUpdateHistoryRepository = new ReliabilityUpdateHistoryRepositoryImpl(jpaReliabilityUpdateHistoryRepository);
    }

    @Test
    void 신뢰도_업데이트_기록을_저장한다() {
        // given
        final ReliabilityUpdateHistory reliabilityUpdateHistory = new ReliabilityUpdateHistory();

        // when
        final ReliabilityUpdateHistory actual = reliabilityUpdateHistoryRepository.save(reliabilityUpdateHistory);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 신뢰도_업데이트_기록이_없으면_빈_Optional을_반환한다() {
        // when
        final Optional<ReliabilityUpdateHistory> actual = reliabilityUpdateHistoryRepository.findFirstByOrderByIdDesc();

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 신뢰도_업데이트_기록의_아이디가_가장_큰_것을_조회한다() {
        // given
        final ReliabilityUpdateHistory history1 = new ReliabilityUpdateHistory();
        final ReliabilityUpdateHistory history2 = new ReliabilityUpdateHistory();
        final ReliabilityUpdateHistory history3 = new ReliabilityUpdateHistory();

        reliabilityUpdateHistoryRepository.save(history1);
        reliabilityUpdateHistoryRepository.save(history2);
        reliabilityUpdateHistoryRepository.save(history3);

        // when
        final Optional<ReliabilityUpdateHistory> actual = reliabilityUpdateHistoryRepository.findFirstByOrderByIdDesc();

        // then
        assertThat(actual).contains(history3);
    }
}
