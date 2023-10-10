package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.user.domain.ReliabilityUpdateHistory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaReliabilityUpdateHistoryRepositoryTest {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaReliabilityUpdateHistoryRepository reliabilityUpdateHistoryRepository;

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

        reliabilityUpdateHistoryRepository.saveAll(List.of(history1, history2, history3));

        em.flush();
        em.clear();

        // when
        final Optional<ReliabilityUpdateHistory> actual = reliabilityUpdateHistoryRepository.findFirstByOrderByIdDesc();

        // then
        assertThat(actual).contains(history3);
    }
}
