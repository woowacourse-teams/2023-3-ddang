package com.ddang.ddang.image.presentation.util.fixture;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

@SuppressWarnings("NonAsciiCharacters")
public class ImageUrlCalculatorFixture {

    protected ImageRelativeUrl 프로필_이미지_상대_URL = ImageRelativeUrl.USER;
    protected ImageRelativeUrl 경매_이미지_상대_URL = ImageRelativeUrl.AUCTION;

    protected String 프로필_이미지_절대_URL = "/users/images/";
    protected String 프로필_기본_이미지_전체_URL = 프로필_이미지_절대_URL + "default_profile_image.png";
    protected String 프로필_이미지_저장_이름 = "profile_image_store_name.png";
    protected String 프로필_이미지_저장_이름이_null = null;
    protected String 프로필_이미지_저장_이름_기반_전체_URL = 프로필_이미지_절대_URL + 프로필_이미지_저장_이름;
    protected String 이미지_저장_이름_기반_프로필_기본_이미지_전체_URL = 프로필_이미지_절대_URL + "default_profile_image.png";
    protected String 경매_이미지_절대_URL = "/auctions/images/";
    protected String 경매_이미지_저장_이름 = "auction_image_store_name.png";
    protected String 경매_이미지_저장_이름_기반_전체_URL = 경매_이미지_절대_URL + 경매_이미지_저장_이름;
}
