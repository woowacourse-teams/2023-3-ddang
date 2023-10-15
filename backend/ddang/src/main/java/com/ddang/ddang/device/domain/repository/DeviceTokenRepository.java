package com.ddang.ddang.device.domain.repository;

import com.ddang.ddang.device.domain.DeviceToken;

import java.util.Optional;

public interface DeviceTokenRepository {

    DeviceToken save(final DeviceToken deviceToken);

    Optional<DeviceToken> findByUserId(final Long userId);

    void deleteByUserId(final Long id);
}
