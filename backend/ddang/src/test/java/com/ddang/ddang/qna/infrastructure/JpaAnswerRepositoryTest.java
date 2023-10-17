package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.qna.domain.Answer;
import com.ddang.ddang.qna.infrastructure.fixture.JpaAnswerRepositoryFixture;
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
class JpaAnswerRepositoryTest extends JpaAnswerRepositoryFixture {

    @Autowired
    JpaAnswerRepository answerRepository;

    @Test
    void 답변을_저장한다() {
        // given
        final Answer answer = new Answer(판매자, 답변_내용);
        질문.addAnswer(answer);

        // when
        final Answer actual = answerRepository.save(answer);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 이미_질문에_대한_답변이_존재한다면_참을_반환한다() {
        // when
        final boolean actual = answerRepository.existsByQuestionId(답변이_존재하는_질문.getId());

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 이미_질문에_대한_답변이_존재하지_않는다면_거짓을_반환한다() {
        // when
        final boolean actual = answerRepository.existsByQuestionId(답변이_존재하지_않는_질문.getId());

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 삭제된_답변은_조회되지_않는다() {
        // when
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedIsFalse(삭제된_답변.getId());

        // then
        assertThat(actual).isEmpty();
    }

    @Test
    void 삭제되지_않은_답변은_조회된다() {
        // when
        final Optional<Answer> actual = answerRepository.findByIdAndDeletedIsFalse(답변.getId());

        // then
        assertThat(actual).contains(답변);
    }
}
