package com.ddang.ddang.device.domain;

import com.ddang.ddang.device.domain.fixture.DeviceTokenFixture;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenTest extends DeviceTokenFixture {

    @Test
    void 디바이스_토큰이_다르다면_참을_반환한다() {
        // given
        final DeviceToken deviceToken = new DeviceToken(사용자, 디바이스_토큰);

        // when
        final boolean actual = deviceToken.isDifferentToken(새로운_디바이스_토큰);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    void 디바이스_토큰이_같다면_거짓을_반환한다() {
        // given
        final DeviceToken deviceToken = new DeviceToken(사용자, 디바이스_토큰);

        // when
        final boolean actual = deviceToken.isDifferentToken(디바이스_토큰);

        // then
        assertThat(actual).isFalse();
    }

    @Test
    void 디바이스_토큰을_갱신한다() {
        // given
        final DeviceToken deviceToken = new DeviceToken(사용자, 디바이스_토큰);

        // when
        deviceToken.updateDeviceToken(새로운_디바이스_토큰);

        // then
        assertThat(deviceToken.getDeviceToken()).isEqualTo(새로운_디바이스_토큰);
    }
}
