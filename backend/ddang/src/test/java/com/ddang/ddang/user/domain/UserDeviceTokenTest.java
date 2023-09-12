package com.ddang.ddang.user.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserDeviceTokenTest {

    @Test
    void 디바이스_토큰이_다르다면_참을_반환한다() {
        // given
        final String deviceToken = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final UserDeviceToken userDeviceToken = new UserDeviceToken(user, deviceToken);

        final String targetDeviceToken = "differentDeviceToken";

        // when
        final boolean actual = userDeviceToken.isDifferentToken(targetDeviceToken);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 디바이스_토큰이_같다면_거짓을_반환한다() {
        // given
        final String deviceToken = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final UserDeviceToken userDeviceToken = new UserDeviceToken(user, deviceToken);

        final String targetDeviceToken = deviceToken;

        // when
        final boolean actual = userDeviceToken.isDifferentToken(targetDeviceToken);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 디바이스_토큰을_갱신한다() {
        // given
        final String deviceToken = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage("profile.png")
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        final UserDeviceToken userDeviceToken = new UserDeviceToken(user, deviceToken);

        final String newDeviceToken = "newDeviceToken";

        // when
        userDeviceToken.updateDeviceToken(newDeviceToken);

        // then
        assertThat(userDeviceToken.getDeviceToken()).isEqualTo(newDeviceToken);
    }
}
