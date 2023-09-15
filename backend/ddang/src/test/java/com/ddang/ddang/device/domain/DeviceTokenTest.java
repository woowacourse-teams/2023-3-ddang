package com.ddang.ddang.device.domain;

import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.domain.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DeviceTokenTest {

    @Test
    void 디바이스_토큰이_다르다면_참을_반환한다() {
        // given
        final String deviceTokenValue = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final DeviceToken deviceToken = new DeviceToken(user, deviceTokenValue);

        final String targetDeviceTokenValue = "differentDeviceToken";

        // when
        final boolean actual = deviceToken.isDifferentToken(targetDeviceTokenValue);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 디바이스_토큰이_같다면_거짓을_반환한다() {
        // given
        final String deviceTokenValue = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final DeviceToken deviceToken = new DeviceToken(user, deviceTokenValue);

        final String targetDeviceTokenValue = deviceTokenValue;

        // when
        final boolean actual = deviceToken.isDifferentToken(targetDeviceTokenValue);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 디바이스_토큰을_갱신한다() {
        // given
        final String deviceTokenValue = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final DeviceToken deviceToken = new DeviceToken(user, deviceTokenValue);

        final String newDeviceTokenValue = "newDeviceToken";

        // when
        deviceToken.updateDeviceToken(newDeviceTokenValue);

        // then
        assertThat(deviceToken.getDeviceToken()).isEqualTo(newDeviceTokenValue);
    }
}
