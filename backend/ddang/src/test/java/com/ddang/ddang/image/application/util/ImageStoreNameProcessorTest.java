package com.ddang.ddang.image.application.util;

import static org.assertj.core.api.Assertions.assertThat;

import com.ddang.ddang.image.application.util.fixture.ImageStoreNameProcessorFixture;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageStoreNameProcessorTest extends ImageStoreNameProcessorFixture {

    @Test
    void 경매_이미지가_null이_아니라면_경매_이미지_아이디를_반환한다() {
        // when
        final String actual = ImageStoreNameProcessor.process(경매_이미지);

        // then
        assertThat(actual).isEqualTo(경매_이미지_이름);
    }

    @Test
    void 경매_이미지가_null이면_경매_이미지_아이디를_null로_반환한다() {
        // when
        final String actual = ImageStoreNameProcessor.process(null인_경매_이미지);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 프로필_이미지가_null이_아니라면_프로필_이미지_아이디를_반환한다() {
        // when
        final String actual = ImageStoreNameProcessor.process(프로필_이미지);

        // then
        assertThat(actual).isEqualTo(프로필_이미지_이름);
    }

    @Test
    void 프로필_이미지가_null이면_프로필_이미지_아이디를_null로_반환한다() {
        // when
        final String actual = ImageStoreNameProcessor.process(null인_프로필_이미지);

        // then
        assertThat(actual).isNull();
    }
}
