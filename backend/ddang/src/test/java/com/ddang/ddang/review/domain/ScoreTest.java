package com.ddang.ddang.review.domain;

import com.ddang.ddang.review.domain.exception.InvalidScoreException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ScoreTest {

    @ParameterizedTest(name = "평가 점수가 {0}점일 때")
    @ValueSource(doubles = {0.5d, 1.0d})
    void 평가_점수가_0점5_단위라면_정상적으로_생성된다(double scoreValue) {
        // when & then
        assertThatCode(() -> new Score(scoreValue)).doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "평가 점수가 {0}점일 때")
    @ValueSource(doubles = {0.1d, 0.9d})
    void 평가_점수가_0점5_단위가_아니라면_예외가_발생한다(double invalidScoreValue) {
        // when & then
        assertThatThrownBy(() -> new Score(invalidScoreValue))
                .isInstanceOf(InvalidScoreException.class)
                .hasMessage("평가 점수는 0.5 단위여야 합니다.");
    }
}
