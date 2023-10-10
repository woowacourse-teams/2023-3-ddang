package com.ddang.ddang.image.application.util;

import com.ddang.ddang.image.application.util.fixture.ImageIdProcessorFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageIdProcessorTest extends ImageIdProcessorFixture {

    @Test
    void 경매_이미지가_null이_아니라면_경매_이미지_아이디를_반환한다() {
        // given
        given(경매_이미지.getId()).willReturn(경매_이미지_아이디);

        // when
        final Long actual = ImageIdProcessor.process(경매_이미지);

        // then
        assertThat(actual).isEqualTo(경매_이미지_아이디);
    }

    @Test
    void 경매_이미지가_null이면_경매_이미지_아이디를_null로_반환한다() {
        // when
        final Long actual = ImageIdProcessor.process(null인_경매_이미지);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 프로필_이미지가_null이_아니라면_프로필_이미지_아이디를_반환한다() {
        // given
        given(프로필_이미지.getId()).willReturn(프로필_이미지_아이디);

        // when
        final Long actual = ImageIdProcessor.process(프로필_이미지);

        // then
        assertThat(actual).isEqualTo(프로필_이미지_아이디);
    }

    @Test
    void 프로필_이미지가_null이면_프로필_이미지_아이디를_null로_반환한다() {
        // when
        final Long actual = ImageIdProcessor.process(null인_프로필_이미지);

        // then
        assertThat(actual).isNull();
    }
}
