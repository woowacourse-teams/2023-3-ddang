package com.ddang.ddang.user.application.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.image.domain.dto.StoreImageDto;
import com.ddang.ddang.user.application.dto.UpdateUserDto;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@SuppressWarnings("NonAsciiCharacters")
public class UserServiceFixture {

    @Autowired
    private UserRepository userRepository;

    protected Long 존재하지_않는_사용자_아이디 = -999L;

    protected String 사용자_이름 = "사용자";
    protected ProfileImage 프로필_이미지;
    protected User 사용자;
    protected StoreImageDto 새로운_프로필_이미지_dto;
    protected UpdateUserDto 사용자_정보_수정_요청_dto;
    protected UpdateUserDto 사용자_이름만_수정_요청_dto;
    protected UpdateUserDto 이미_존재하는_사용자_이름으로_수정_요청_dto;
    protected UpdateUserDto 사용자_이미지만_수정_요청_dto;

    @BeforeEach
    void setUp() {
        프로필_이미지 = new ProfileImage("upload.png", "store-name.png");
        final String 이미_존재하는_사용자_이름 = "중복되는 이름";
        final User 이미_저장된_사용자 = User.builder()
                                    .name(이미_존재하는_사용자_이름)
                                    .profileImage(프로필_이미지)
                                    .reliability(new Reliability(4.7d))
                                    .oauthId("12345")
                                    .build();
        사용자 = User.builder()
                  .name(사용자_이름)
                  .profileImage(프로필_이미지)
                  .reliability(new Reliability(4.7d))
                  .oauthId("12346")
                  .build();

        userRepository.save(이미_저장된_사용자);
        userRepository.save(사용자);

        final MockMultipartFile 새로운_이미지_파일 = new MockMultipartFile(
                "profileImage",
                "updateImage.png",
                MediaType.IMAGE_PNG.toString(),
                new byte[]{1}
        );
        새로운_프로필_이미지_dto = new StoreImageDto(
                새로운_이미지_파일.getOriginalFilename(),
                새로운_이미지_파일.getOriginalFilename()
        );

        사용자_정보_수정_요청_dto = new UpdateUserDto("updateName", 새로운_이미지_파일);
        사용자_이름만_수정_요청_dto = new UpdateUserDto("updateName", null);
        이미_존재하는_사용자_이름으로_수정_요청_dto = new UpdateUserDto(이미_존재하는_사용자_이름, null);
        사용자_이미지만_수정_요청_dto = new UpdateUserDto(null, 새로운_이미지_파일);
    }
}
