package com.ddang.ddang.device.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.device.application.fixture.DeviceTokenServiceFixture;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenServiceTest extends DeviceTokenServiceFixture {

    @Autowired
    DeviceTokenService deviceTokenService;

    @Autowired
    JpaDeviceTokenRepository deviceTokenRepository;

    @Test
    void 사용자의_디바이스_토큰이_존재하지_않는다면_저장한다() {
        // when & then
        assertThatNoException().isThrownBy(
                () -> deviceTokenService.persist(디바이스_토큰이_없는_사용자.getId(), 디바이스_토큰_저장을_위한_DTO)
        );
    }

    @Test
    void 사용자의_디바이스_토큰이_이미_존재하고_새로운_토큰이_주어진다면_토큰을_갱신한다() {
        // when
        deviceTokenService.persist(디바이스_토큰이_있는_사용자.getId(), 디바이스_토큰_갱신을_위한_DTO);

        // then
        final Optional<DeviceToken> deviceTokenResult = deviceTokenRepository.findByUserId(디바이스_토큰이_있는_사용자.getId());
        final String actual = deviceTokenResult.get().getDeviceToken();

        assertThat(actual).isEqualTo(갱신된_디바이스_토큰_값);
    }

    @Test
    void 사용자의_디바이스_토큰이_이미_존재하고_동일한_토큰이_주어진다면_토큰을_갱신하지_않는다() {
        // when
        deviceTokenService.persist(디바이스_토큰이_있는_사용자.getId(), 존재하는_디바이스_토큰과_동일한_토큰을_저장하려는_DTO);

        // then
        final Optional<DeviceToken> userDeviceToken = deviceTokenRepository.findByUserId(디바이스_토큰이_있는_사용자.getId());
        final String actual = userDeviceToken.get().getDeviceToken();

        assertThat(actual).isEqualTo(사용_중인_디바이스_토큰_값);
    }

    @Test
    void 사용자를_찾을_수_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> deviceTokenService.persist(존재하지_않는_사용자_아이디, 디바이스_토큰_저장을_위한_DTO))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }
}
