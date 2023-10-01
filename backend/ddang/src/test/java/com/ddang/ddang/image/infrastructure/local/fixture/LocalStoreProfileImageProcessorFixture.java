package com.ddang.ddang.image.infrastructure.local.fixture;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.Mockito.mock;

@SuppressWarnings("NonAsciiCharacters")
public class LocalStoreProfileImageProcessorFixture {

    protected MockMultipartFile 빈_이미지_파일 = new MockMultipartFile("image.png", new byte[0]);
    protected MultipartFile 이미지_파일 = mock(MultipartFile.class);
    protected String 기존_이미지_파일명 = "image.png";
    protected String 지원하지_않는_확장자를_가진_이미지_파일명 = "image.gif";

}
