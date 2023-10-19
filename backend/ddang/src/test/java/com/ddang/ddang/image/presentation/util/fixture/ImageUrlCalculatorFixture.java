package com.ddang.ddang.image.presentation.util.fixture;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

@SuppressWarnings("NonAsciiCharacters")
public class ImageUrlCalculatorFixture {

    protected ImageRelativeUrl 프로필_이미지_상대_URL = ImageRelativeUrl.USER;
    protected ImageRelativeUrl 경매_이미지_상대_URL = ImageRelativeUrl.AUCTION;

    protected String 프로필_이미지_절대_URL = "/users/images/";
    protected Long 프로필_이미지_아이디 = 2L;
    protected Long 프로필_이미지_아이디가_null = null;
    protected String 프로필_이미지_전체_URL = 프로필_이미지_절대_URL + 프로필_이미지_아이디;
    protected String 프로필_기본_이미지_전체_URL = 프로필_이미지_절대_URL + "1";
    protected String 경매_이미지_절대_URL = "/auctions/images/";
    protected Long 경매_이미지_아이디 = 1L;
    protected String 경매_이미지_전체_URL = 경매_이미지_절대_URL + 경매_이미지_아이디;
}
