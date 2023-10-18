package com.ddang.ddang.image.presentation.fixture;

import com.ddang.ddang.configuration.CommonControllerSliceTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

@SuppressWarnings("NonAsciiCharacters")
public class ImageControllerFixture extends CommonControllerSliceTest {

    protected byte[] 이미지_파일_바이트 = "이것은 이미지 파일의 바이트 코드입니다.".getBytes();
    protected Resource 이미지_파일_리소스 = new ByteArrayResource(이미지_파일_바이트);
    protected String 프로필_이미지_이름 = "profile_image.png";
    protected String 존재하지_않는_프로필_이미지_이름 = "invalid_profile_image.png";
    protected String 경매_이미지_이름 = "auction_image.png";
    protected String 존재하지_않는_경매_이미지_이름 = "invalid_auction_image.png";
}
