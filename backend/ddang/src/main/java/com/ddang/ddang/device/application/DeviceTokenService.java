package com.ddang.ddang.device.application;

import com.ddang.ddang.device.application.dto.UpdateDeviceTokenDto;
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

    // TODO 메서드 이름이랑 반환값 어떻게 하는게 좋을까요?
    @Transactional
    public void createOrUpdate(final Long userId, final UpdateDeviceTokenDto deviceTokenDto) {
        final User findUser = userRepository.findById(userId)
                                            .orElseThrow(() -> new UserNotFoundException("해당 사용자를 찾을 수 없습니다."));

        updateOrPersistDeviceToken(findUser, deviceTokenDto.deviceToken());
    }

    private void updateOrPersistDeviceToken(final User persistedUser, final String newDeviceToken) {
        final DeviceToken deviceToken =
                deviceTokenRepository.findByUserId(persistedUser.getId())
                                     .orElseGet(() -> {
                                         final DeviceToken newEntity =
                                                 new DeviceToken(persistedUser, newDeviceToken);

                                         return deviceTokenRepository.save(newEntity);
                                     });
        if (deviceToken.isDifferentToken(newDeviceToken)) {
            deviceToken.updateDeviceToken(newDeviceToken);
        }
    }
}
