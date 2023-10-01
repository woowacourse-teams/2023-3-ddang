package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.infrastructure.persistence.fixture.JpaMessageRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
class JpaMessageRepositoryTest extends JpaMessageRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaMessageRepository messageRepository;

    @Test
    void 메시지를_저장한다() {
        // when
        messageRepository.save(메시지);

        em.flush();
        em.clear();

        // then
        assertThat(메시지.getId()).isPositive();
    }

    @Test
    void 조회하려는_메시지_아이디가_존재하지_않는_경우_거짓을_반환한다() {
        // when & then
        assertThat(messageRepository.existsById(invalidMessageId)).isFalse();
    }
}
