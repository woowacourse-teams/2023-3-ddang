package com.ddang.ddang.image.presentation.util.fixture;

import com.ddang.ddang.image.presentation.util.ImageRelativeUrl;

@SuppressWarnings("NonAsciiCharacters")
public class ImageUrlCalculatorFixture {

    protected ImageRelativeUrl 프로필_이미지_상대_URL = ImageRelativeUrl.USER;
    protected ImageRelativeUrl 경매_이미지_상대_URL = ImageRelativeUrl.AUCTION;

    // TODO: 10/16/23 [고민] ServletUriComponentsBuilder.fromCurrentContextPath()로 값을 가져오다 보니, 환경에 따라 테스트 성공 여부가 달라질 것 같아 아래와 같이 했는데 괜찮을까요? + 이해가 될까요?
    protected String 도메인_제외_프로필_이미지_절대_URL = "/users/images/";
    protected Long 프로필_이미지_아이디 = 2L;
    protected Long 프로필_이미지_아이디가_null = null;
    protected String 프로필_이미지_전체_URL = 도메인_제외_프로필_이미지_절대_URL + 프로필_이미지_아이디;
    protected String 프로필_기본_이미지_전체_URL = 도메인_제외_프로필_이미지_절대_URL + "1";
    protected String 도메인_제외_경매_이미지_절대_URL = "/auctions/images/";
    protected Long 경매_이미지_아이디 = 1L;
    protected String 경매_이미지_전체_URL = 도메인_제외_경매_이미지_절대_URL + 경매_이미지_아이디;
}
