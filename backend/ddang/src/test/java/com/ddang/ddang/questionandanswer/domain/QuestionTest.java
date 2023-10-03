package com.ddang.ddang.questionandanswer.domain;

import com.ddang.ddang.questionandanswer.domain.fixture.QuestionFixture;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

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
}
