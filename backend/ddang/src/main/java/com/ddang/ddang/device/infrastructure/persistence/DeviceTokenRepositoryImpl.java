package com.ddang.ddang.device.infrastructure.persistence;

import com.ddang.ddang.device.domain.DeviceToken;
import com.ddang.ddang.device.domain.repository.DeviceTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DeviceTokenRepositoryImpl implements DeviceTokenRepository {

    private final JpaDeviceTokenRepository jpaDeviceTokenRepository;

    @Override
    public DeviceToken save(final DeviceToken deviceToken) {
        return jpaDeviceTokenRepository.save(deviceToken);
    }

    @Override
    public Optional<DeviceToken> findByUserId(final Long userId) {
        return jpaDeviceTokenRepository.findByUserId(userId);
    }

    @Override
    public void deleteByUserId(final Long id) {
        jpaDeviceTokenRepository.deleteByUserId(id);
    }
}
