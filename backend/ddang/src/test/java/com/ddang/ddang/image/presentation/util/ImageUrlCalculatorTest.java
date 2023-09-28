package com.ddang.ddang.image.presentation.util;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageUrlCalculatorTest {

    @Test
    void 프로필_사진의_URL을_계산한다() {
        // given
        final Long profileImageId = 1L;
        final String absoluteUrl = "http://3-ddang.store/users/images/";

        // when
        final String actual = ImageUrlCalculator.calculateBy(absoluteUrl, profileImageId);

        // then
        assertThat(actual).isEqualTo(absoluteUrl + profileImageId);
    }

    @Test
    void 이미지_아이디가_null인_경우_프로필_사진의_URL로_null을_반환한다() {
        // given
        final Long emptyProfileImage = null;
        final String absoluteUrl = "http://3-ddang.store/users/images/";

        // when
        final String actual = ImageUrlCalculator.calculateBy(absoluteUrl, emptyProfileImage);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 경매_대표_이미지의_URL을_계산한다() {
        // given
        final Long auctionImageId = 1L;
        final String absoluteUrl = "http://3-ddang.store/users/images/";

        // when
        final String actual = ImageUrlCalculator.calculateBy(absoluteUrl, auctionImageId);

        // then
        assertThat(actual).isEqualTo(absoluteUrl + auctionImageId);
    }

    @Test
    void 이미지_아이디가_null인_경우_경매_대표_이미지의_URL로_null을_반환한다() {
        // given
        final Long emptyAuctionImageId = null;
        final String absoluteUrl = "http://3-ddang.store/users/images/";

        // when
        final String actual = ImageUrlCalculator.calculateBy(absoluteUrl, emptyAuctionImageId);

        // then
        assertThat(actual).isNull();
    }
}
