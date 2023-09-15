package com.ddang.ddang.device.application;

import com.ddang.ddang.configuration.IsolateDatabase;
import com.ddang.ddang.device.application.dto.PersistDeviceTokenDto;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.image.domain.ProfileImage;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@IsolateDatabase
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@SuppressWarnings("NonAsciiCharacters")
class DeviceTokenServiceTest {

    @Autowired
    DeviceTokenService deviceTokenService;

    @Autowired
    JpaDeviceTokenRepository deviceTokenRepository;

    @Autowired
    JpaUserRepository userRepository;

    @Test
    void 사용자의_디바이스_토큰이_존재하지_않는다면_저장한다() {
        // given
        final String deviceTokenValue = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        userRepository.save(user);

        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(deviceTokenValue);

        // when & then
        assertThatNoException().isThrownBy(() -> deviceTokenService.persist(user.getId(), persistDeviceTokenDto));
    }

    @Test
    void 사용자의_디바이스_토큰이_이미_존재하고_새로운_토큰이_주어진다면_토큰을_갱신한다() {
        // given
        final String deviceTokenValue = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        userRepository.save(user);

        final DeviceToken deviceToken = new DeviceToken(user, deviceTokenValue);
        deviceTokenRepository.save(deviceToken);

        final String newDeviceTokenValue = "newDeviceToken";
        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(newDeviceTokenValue);

        // when
        deviceTokenService.persist(user.getId(), persistDeviceTokenDto);

        // then
        assertThat(deviceToken.getDeviceToken()).isEqualTo(newDeviceTokenValue);
    }

    @Test
    void 사용자의_디바이스_토큰이_이미_존재하고_동일한_토큰이_주어진다면_토큰을_갱신하지_않는다() {
        // given
        final String deviceTokenValue = "deviceToken";
        final User user = User.builder()
                              .name("사용자")
                              .profileImage(new ProfileImage("upload.png", "store.png"))
                              .reliability(4.7d)
                              .oauthId("12345")
                              .build();
        userRepository.save(user);

        final DeviceToken deviceToken = new DeviceToken(user, deviceTokenValue);
        deviceTokenRepository.save(deviceToken);

        final String newDeviceTokenValue = deviceTokenValue;
        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(newDeviceTokenValue);

        // when
        deviceTokenService.persist(user.getId(), persistDeviceTokenDto);

        // then
        assertThat(deviceToken.getDeviceToken()).isEqualTo(deviceTokenValue);
    }

    @Test
    void 사용자를_찾을_수_없다면_예외가_발생한다() {
        // given
        final Long invalidUserId = -999L;
        final String deviceTokenValue = "deviceToken";
        final PersistDeviceTokenDto persistDeviceTokenDto = new PersistDeviceTokenDto(deviceTokenValue);

        // when & then
        assertThatThrownBy(() -> deviceTokenService.persist(invalidUserId, persistDeviceTokenDto))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("해당 사용자를 찾을 수 없습니다.");
    }
}
