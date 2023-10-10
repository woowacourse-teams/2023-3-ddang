package com.ddang.ddang.device.domain.fixture;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.Reliability;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.BeforeEach;

@SuppressWarnings("NonAsciiCharacters")
public class DeviceTokenFixture {

    protected String 디바이스_토큰 = "deviceToken";
    protected String 새로운_디바이스_토큰 = "newDeviceToken";
    protected User 사용자;

    @BeforeEach
    void setUp() {
        사용자 = User.builder()
                    .name("사용자")
                    .profileImage(new ProfileImage("upload.png", "store.png"))
                    .reliability(new Reliability(4.7d))
                    .oauthId("12345")
                    .build();
    }
}
