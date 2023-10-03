package com.ddang.ddang.questionandanswer.infrastructure;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.questionandanswer.domain.Question;
import com.ddang.ddang.questionandanswer.infrastructure.fixture.JpaQuestionRepositoryFixture;
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
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaQuestionRepositoryTest extends JpaQuestionRepositoryFixture {

    @PersistenceContext
    EntityManager em;

    @Autowired
    JpaQuestionRepository questionRepository;

    @Test
    void 질문을_저장한다() {
        // given
        final Question question = new Question(경매, 질문자, 질문_내용);

        // when
        final Question actual = questionRepository.save(question);

        // then
        em.flush();
        em.clear();

        assertThat(actual.getId()).isPositive();
    }

}
