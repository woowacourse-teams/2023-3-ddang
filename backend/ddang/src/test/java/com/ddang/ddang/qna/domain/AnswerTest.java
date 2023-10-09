package com.ddang.ddang.qna.domain;

import com.ddang.ddang.qna.domain.fixture.AnswerFixture;
import com.ddang.ddang.user.domain.User;
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

    @Test
    void 답변의_작성자_조회시_질문에_해당하는_경매의_판매자_정보를_가져온다() {
        // when
        final User actual = 답변.getWriter();

        // then
        assertThat(actual).isEqualTo(판매자);
    }
}
