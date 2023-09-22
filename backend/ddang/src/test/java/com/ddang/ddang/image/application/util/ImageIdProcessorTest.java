package com.ddang.ddang.image.application.util;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class ImageIdProcessorTest {

    @Test
    void 경매_이미지가_null이_아니라면_경매_이미지_아이디를_반환한다() {
        // given
        final AuctionImage mockAuctionImage = mock(AuctionImage.class);
        final long expect = 1L;
        BDDMockito.given(mockAuctionImage.getId()).willReturn(expect);

        // when
        final Long actual = ImageIdProcessor.process(mockAuctionImage);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 경매_이미지가_null이면_경매_이미지_아이디를_null로_반환한다() {
        // given
        final AuctionImage auctionImage = null;

        // when
        final Long actual = ImageIdProcessor.process(auctionImage);

        // then
        assertThat(actual).isNull();
    }

    @Test
    void 프로필_이미지가_null이_아니라면_프로필_이미지_아이디를_반환한다() {
        // given
        final ProfileImage mockProfileImage = mock(ProfileImage.class);
        final long expect = 1L;
        BDDMockito.given(mockProfileImage.getId()).willReturn(expect);

        // when
        final Long actual = ImageIdProcessor.process(mockProfileImage);

        // then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    void 프로필_이미지가_null이면_프로필_이미지_아이디를_null로_반환한다() {
        // given
        final ProfileImage profileImage = null;

        // when
        final Long actual = ImageIdProcessor.process(profileImage);

        // then
        assertThat(actual).isNull();
    }
}
