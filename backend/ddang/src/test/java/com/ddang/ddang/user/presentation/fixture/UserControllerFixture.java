package com.ddang.ddang.user.presentation.fixture;

import com.ddang.ddang.authentication.infrastructure.jwt.PrivateClaims;
import com.ddang.ddang.configuration.CommonControllerSliceTest;
import com.ddang.ddang.user.application.dto.ReadUserDto;
import com.ddang.ddang.user.presentation.dto.request.UpdateUserRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.charset.StandardCharsets;

@SuppressWarnings("NonAsciiCharacters")
public class UserControllerFixture extends CommonControllerSliceTest {

    protected String 액세스_토큰_값 = "Bearer accessToken";
    protected PrivateClaims 사용자_ID_클레임 = new PrivateClaims(1L);
    protected String 탈퇴한_사용자_이름 = "알 수 없음";

    protected ReadUserDto 사용자_정보_조회_dto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", false);
    protected ReadUserDto 탈퇴한_사용자_정보_조회_dto = new ReadUserDto(1L, "사용자1", 1L, 4.6d, "12345", true);
    protected UpdateUserRequest 수정할_이름_request = new UpdateUserRequest("updateName");
    protected ReadUserDto 수정후_사용자_정보_조회_dto = new ReadUserDto(1L, 수정할_이름_request.name(), 1L, 4.6d, "12345", false);
    private String json = "{\"name\":\"" + 수정할_이름_request.name() + "\"}";
    private byte[] jsonBytes = json.getBytes(StandardCharsets.UTF_8);
    protected MockMultipartFile 수정할_이름 = new MockMultipartFile(
            "request",
            "request",
            MediaType.APPLICATION_JSON_VALUE,
            jsonBytes
    );
    protected MockMultipartFile 수정할_프로필_이미지 = new MockMultipartFile(
            "profileImage",
            "image.png",
            MediaType.IMAGE_PNG_VALUE,
            new byte[]{1}
    );
    protected MockMultipartFile 프로필_이미지가_없는_경우_파일 = new MockMultipartFile(
            "profileImage",
            (byte[]) null
    );
    protected MockMultipartFile 이름을_수정하지_않는_경우 = new MockMultipartFile(
            "request",
            (byte[]) null
    );
}
