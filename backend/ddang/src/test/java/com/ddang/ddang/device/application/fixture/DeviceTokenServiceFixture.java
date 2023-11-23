package com.ddang.ddang.device.application.fixture;

import com.ddang.ddang.device.application.dto.request.CreateDeviceTokenDto;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.domain.repository.DeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.domain.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenServiceFixture {

    @Autowired
    private DeviceTokenRepository deviceTokenRepository;

    @Autowired
    private UserRepository userRepository;

    private String 초기_디바이스_토큰_값 = "initialDeviceToken";
    private DeviceToken 사용자의_디바이스_토큰;

    protected String 사용_중인_디바이스_토큰_값 = "usingDeviceToken";
    protected String 갱신된_디바이스_토큰_값 = "newDeviceToken";
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected User 디바이스_토큰이_있는_사용자;
    protected User 디바이스_토큰이_없는_사용자;
    protected CreateDeviceTokenDto 디바이스_토큰_저장을_위한_DTO;
    protected CreateDeviceTokenDto 디바이스_토큰_갱신을_위한_DTO;
    protected CreateDeviceTokenDto 존재하는_디바이스_토큰과_동일한_토큰을_저장하려는_DTO;

    @BeforeEach
    void setUp() {
        디바이스_토큰이_있는_사용자 = User.builder()
                              .name("디바이스 토큰이 있는 사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(4.7d))
                              .oauthId("12345")
                              .build();
        디바이스_토큰이_없는_사용자 = User.builder()
                              .name("디바이스 토큰이 없는 사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(new Reliability(4.7d))
                              .oauthId("12346")
                              .build();
        사용자의_디바이스_토큰 = new DeviceToken(디바이스_토큰이_있는_사용자, 사용_중인_디바이스_토큰_값);

        userRepository.save(디바이스_토큰이_있는_사용자);
        userRepository.save(디바이스_토큰이_없는_사용자);

        deviceTokenRepository.save(사용자의_디바이스_토큰);

        디바이스_토큰_저장을_위한_DTO = new CreateDeviceTokenDto(초기_디바이스_토큰_값);
        디바이스_토큰_갱신을_위한_DTO = new CreateDeviceTokenDto(갱신된_디바이스_토큰_값);
        존재하는_디바이스_토큰과_동일한_토큰을_저장하려는_DTO = new CreateDeviceTokenDto(사용_중인_디바이스_토큰_값);
    }
}
