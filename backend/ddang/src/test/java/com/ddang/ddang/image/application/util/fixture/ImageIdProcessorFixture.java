package com.ddang.ddang.image.application.util.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;

import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class ImageIdProcessorFixture {

    protected AuctionImage 경매_이미지 = mock(AuctionImage.class);
    protected AuctionImage null인_경매_이미지 = null;
    protected Long 경매_이미지_아이디 = 1L;

    protected ProfileImage 프로필_이미지 = mock(ProfileImage.class);
    protected ProfileImage null인_프로필_이미지 = null;
    protected Long 프로필_이미지_아이디 = 1L;
}
