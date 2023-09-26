package com.ddang.ddang.device.application;

import com.ddang.ddang.device.application.dto.PersistDeviceTokenDto;
import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.infrastructure.persistence.JpaDeviceTokenRepository;
import com.ddang.ddang.user.application.exception.UserNotFoundException;
import com.ddang.ddang.user.domain.User;
import com.ddang.ddang.user.infrastructure.persistence.JpaUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DeviceTokenService {

    private final JpaDeviceTokenRepository deviceTokenRepository;
    private final JpaUserRepository userRepository;

    @Transactional
    public void persist(final Long userId, final PersistDeviceTokenDto deviceTokenDto) {
        final String newDeviceToken = deviceTokenDto.deviceToken();
        if (newDeviceToken == null || newDeviceToken.isBlank()) {
            return;
        }

        final DeviceToken deviceToken =
                deviceTokenRepository.findByUserId(userId)
                                     .orElseGet(() -> createDeviceToken(userId, newDeviceToken));
        if (deviceToken.isDifferentToken(newDeviceToken)) {
            deviceToken.updateDeviceToken(newDeviceToken);
        }
    }

    private DeviceToken createDeviceToken(final Long userId, final String newDeviceToken) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));
        final DeviceToken deviceToken = new DeviceToken(findUser, newDeviceToken);

        return deviceTokenRepository.save(deviceToken);
    }
}
