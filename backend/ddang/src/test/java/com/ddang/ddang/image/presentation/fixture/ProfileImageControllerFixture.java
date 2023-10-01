package com.ddang.ddang.image.presentation.fixture;

import com.ddang.ddang.configuration.CommonControllerSliceTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

@SuppressWarnings("NonAsciiCharacters")
public class ProfileImageControllerFixture extends CommonControllerSliceTest {

    protected byte[] 이미지_파일_바이트 = "이것은 이미지 파일의 바이트 코드입니다.".getBytes();
    protected Resource 이미지_파일_리소스 = new ByteArrayResource(이미지_파일_바이트);
    protected Long 프로필_이미지_아이디 = 1L;
    protected Long 존재하지_않는_프로필_이미지_아이디 = -999L;
    protected Long 경매_이미지_아이디 = 1L;
    protected Long 존재하지_않는_경매_이미지_아이디 = -999L;
}
