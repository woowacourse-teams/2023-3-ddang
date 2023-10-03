package com.ddang.ddang.questionandanswer.domain;

import com.ddang.ddang.questionandanswer.domain.fixture.AnswerFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class AnswerTest extends AnswerFixture {

    @Test
    void 답변과_질문의_연관관계를_세팅한다() {
        // given
        final Answer answer = new Answer("답변드립니다.");

        // when
        answer.initQuestion(질문);

        // then
        assertThat(answer.getQuestion()).isEqualTo(질문);
    }
}
