package com.ddang.ddang.image.presentation.util;

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
class ImageUrlCalculatorTest {

    @Test
    void 프로필_사진의_URL을_계산한다() {
        // given
        final ProfileImage mockProfileImage = mock(ProfileImage.class);
        final long profileImageId = 1L;
        BDDMockito.given(mockProfileImage.getId()).willReturn(profileImageId);
        final String baseUrl = "/baseUrl/";

        // when
        final String actual = ImageUrlCalculator.calculateProfileImageUrl(mockProfileImage, baseUrl);

        // then
        assertThat(actual).isEqualTo(baseUrl + profileImageId);
    }

    @Test
    void 경매_대표_이미지의_URL을_계산한다() {
        // given
        final AuctionImage mockAuctionImage = mock(AuctionImage.class);
        final long auctionImageId = 1L;
        BDDMockito.given(mockAuctionImage.getId()).willReturn(auctionImageId);
        final String baseUrl = "/baseUrl/";

        // when
        final String actual = ImageUrlCalculator.calculateAuctionImageUrl(mockAuctionImage, baseUrl);

        // then
        assertThat(actual).isEqualTo(baseUrl + auctionImageId);
    }
}
