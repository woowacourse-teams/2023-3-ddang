package com.ddang.ddang.qna.domain;

import com.ddang.ddang.qna.domain.fixture.QuestionFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class QuestionTest extends QuestionFixture {

    @Test
    void 질문과_답변의_연관관계를_세팅한다() {
        // when
        질문.addAnswer(답변);

        // then
        SoftAssertions.assertSoftly(softAssertions -> {
            softAssertions.assertThat(질문.getAnswer()).isEqualTo(답변);
            softAssertions.assertThat(답변.getQuestion()).isEqualTo(질문);
        });
    }

    @Test
    void 답변_가능한_사용자인지_확인할때_판매자인_경우_참을_반환한다() {
        // when
        final boolean actual = 질문.isAnsweringAllowed(판매자);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 답변_가능한_사용자인지_확인할때_판매자가_아닌_경우_거짓을_반환한다() {
        // when
        final boolean actual = 질문.isAnsweringAllowed(판매자가_아닌_사용자);

        // then
        assertThat(actual).isFalse();
    }
}
