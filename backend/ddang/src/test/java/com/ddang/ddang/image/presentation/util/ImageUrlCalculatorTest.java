package com.ddang.ddang.image.presentation.util;

import com.ddang.ddang.image.presentation.util.fixture.ImageUrlCalculatorFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageUrlCalculatorTest extends ImageUrlCalculatorFixture {

    @Test
    void 프로필_사진의_URL을_계산한다() {
        // when
        final String actual = ImageUrlCalculator.calculateBy(프로필_이미지_절대_URL, 프로필_이미지_아이디);

        // then
        assertThat(actual).isEqualTo(프로필_이미지_전체_URL);
    }

    @Test
    void 경매_대표_이미지의_URL을_계산한다() {
        // when
        final String actual = ImageUrlCalculator.calculateBy(경매_이미지_절대_URL, 경매_이미지_아이디);

        // then
        assertThat(actual).isEqualTo(경매_이미지_전체_URL);
    }
}
