package com.ddang.ddang.qna.infrastructure;

import com.ddang.ddang.configuration.JpaConfiguration;
import com.ddang.ddang.configuration.QuerydslConfiguration;
import com.ddang.ddang.qna.domain.Question;
import com.ddang.ddang.qna.infrastructure.fixture.JpaQuestionRepositoryFixture;
import org.assertj.core.api.*;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({JpaConfiguration.class, QuerydslConfiguration.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class JpaQuestionRepositoryTest extends JpaQuestionRepositoryFixture {

    @Autowired
    JpaQuestionRepository questionRepository;

    @Test
    void 질문을_저장한다() {
        // given
        final Question question = new Question(경매, 질문자, 질문_내용);

        // when
        final Question actual = questionRepository.save(question);

        // then
        assertThat(actual.getId()).isPositive();
    }

    @Test
    void 경매_아이디를_통해_질문과_답변들을_모두_조회한다() {
        // when
        final List<Question> actual = questionRepository.readAllByAuctionId(질문이_3개_답변이_2개인_경매.getId());

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(actual).hasSize(3);
            softAssertions.assertThat(actual.get(0)).isEqualTo(질문1);
            softAssertions.assertThat(actual.get(0).getAnswer()).isEqualTo(답변1);
            softAssertions.assertThat(actual.get(1)).isEqualTo(질문2);
            softAssertions.assertThat(actual.get(1).getAnswer()).isEqualTo(답변2);
            softAssertions.assertThat(actual.get(2)).isEqualTo(질문3);
            softAssertions.assertThat(actual.get(2).getAnswer()).isNull();
        });
    }
}
