package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.ReadMessageLog;
import com.ddang.ddang.chat.domain.repository.ReadMessageLogRepository;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.ReadMessageLogRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
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
class ReadMessageLogRepositoryImplTest extends ReadMessageLogRepositoryFixture {

    ReadMessageLogRepository readMessageLogRepository;

    @BeforeEach
    void setUp(@Autowired final JpaReadMessageLogRepository jpaReadMessageLogRepository) {
        readMessageLogRepository = new ReadMessageLogRepositoryImpl(jpaReadMessageLogRepository);
    }

    @Test
    void 마지막_읽은_메시지를_저장한다() {
        // given
        final ReadMessageLog actual = readMessageLogRepository.saveAll(List.of(다섯_번째_메시지까지_읽은_메시지_로그)).get(0);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 메시지_조회자_아이디와_채팅방_아이디에_해당하는_조회_메시지_로그를_반환한다() {
        // given
        final Optional<ReadMessageLog> actual = readMessageLogRepository.findBy(메리.getId(), 메리_엔초_채팅방.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).isPresent();
            softAssertions.assertThat(actual.get().getLastReadMessageId()).isEqualTo(다섯_번째_메시지.getId());
        });
    }
}
