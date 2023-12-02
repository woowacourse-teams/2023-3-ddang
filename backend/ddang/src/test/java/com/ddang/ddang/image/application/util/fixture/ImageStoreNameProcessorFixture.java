package com.ddang.ddang.image.application.util.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.Image;
import com.ddang.ddang.image.domain.ProfileImage;

import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class ImageStoreNameProcessorFixture {

    protected AuctionImage 경매_이미지 = mock(AuctionImage.class);
    protected AuctionImage null인_경매_이미지 = null;
    protected ProfileImage 프로필_이미지 = mock(ProfileImage.class);
    protected ProfileImage null인_프로필_이미지 = null;
    protected String 경매_이미지_저장_이름 = "auction_image.png";
    protected String 프로필_이미지_저장_이름 = "profile_image.png";
    protected Image 프로필_이미지_데이터_VO = new Image("upload.png", 프로필_이미지_저장_이름);
    protected Image 경매_이미지_데이터_VO = new Image("upload.png", 경매_이미지_저장_이름);
}
