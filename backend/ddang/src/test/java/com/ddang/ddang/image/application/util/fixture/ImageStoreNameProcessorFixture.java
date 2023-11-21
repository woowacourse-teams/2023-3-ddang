package com.ddang.ddang.image.application.util.fixture;

import com.ddang.ddang.image.domain.AuctionImage;
import com.ddang.ddang.image.domain.ProfileImage;

@SuppressWarnings("NonAsciiCharacters")
public class ImageStoreNameProcessorFixture {

    protected AuctionImage 경매_이미지 = new AuctionImage("upload-name.png", "store-name.png");
    protected AuctionImage null인_경매_이미지 = null;
    protected String 경매_이미지_이름 = "store-name.png";

    protected ProfileImage 프로필_이미지 = new ProfileImage("upload-name.png", "store-name.png");
    protected ProfileImage null인_프로필_이미지 = null;
    protected String 프로필_이미지_이름 = "store-name.png";
}
