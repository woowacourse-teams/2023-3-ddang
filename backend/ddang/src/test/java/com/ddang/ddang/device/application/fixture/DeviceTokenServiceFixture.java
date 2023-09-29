package com.ddang.ddang.device.application.fixture;

import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenServiceFixture {

    @Autowired
    private JpaDeviceTokenRepository deviceTokenRepository;

    @Autowired
    private JpaUserRepository userRepository;

    protected String 디바이스_토큰_값 = "deviceToken";
    protected String 사용_중인_디바이스_토큰_값 = "usingDeviceToken";
    protected String 초기_디바이스_토큰_값 = "initialDeviceToken";
    protected String 갱신된_디바이스_토큰_값 = "newDeviceToken";
    protected Long 존재하지_않는_사용자_아이디 = -999L;
    protected User 디바이스_토큰이_있는_사용자 = User.builder()
                                         .name("디바이스 토큰이 있는 사용자")
                                         .profileImage(new ProfileImage("upload.png", "store.png"))
                                         .reliability(4.7d)
                                         .oauthId("12345")
                                         .build();
    protected User 디바이스_토큰이_없는_사용자 = User.builder()
                                         .name("디바이스 토큰이 없는 사용자")
                                         .profileImage(new ProfileImage("upload.png", "store.png"))
                                         .reliability(4.7d)
                                         .oauthId("12346")
                                         .build();
    protected DeviceToken 사용자의_디바이스_토큰 = new DeviceToken(디바이스_토큰이_있는_사용자, 사용_중인_디바이스_토큰_값);

    @BeforeEach
    void setUp() {
        userRepository.saveAll(List.of(디바이스_토큰이_있는_사용자, 디바이스_토큰이_없는_사용자));
        deviceTokenRepository.save(사용자의_디바이스_토큰);
    }
}
