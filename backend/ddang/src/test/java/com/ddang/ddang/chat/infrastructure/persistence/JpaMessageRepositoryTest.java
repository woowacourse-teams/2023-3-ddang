package com.ddang.ddang.chat.infrastructure.persistence;

import com.ddang.ddang.chat.domain.Message;
import com.ddang.ddang.chat.infrastructure.persistence.fixture.JpaMessageRepositoryFixture;
import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaMessageRepositoryTest extends JpaMessageRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaMessageRepository messageRepository;

    @Test
    void 메시지를_저장한다() {
        // when
        final Message actual = messageRepository.save(메시지);

        em.flush();
        em.clear();

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual.getId()).isPositive();
            softAssertions.assertThat(actual.getContents()).isEqualTo(메시지.getContents());
            softAssertions.assertThat(actual.getWriter()).isEqualTo(메시지.getWriter());
            softAssertions.assertThat(actual.getReceiver()).isEqualTo(메시지.getReceiver());
            softAssertions.assertThat(actual.getChatRoom()).isEqualTo(메시지.getChatRoom());
        });
    }

    @Test
    void 조회하려는_메시지_아이디가_존재하지_않는_경우_거짓을_반환한다() {
        // when & then
        assertThat(messageRepository.existsById(유효하지_않은_메시지_아이디)).isFalse();
    }
}
