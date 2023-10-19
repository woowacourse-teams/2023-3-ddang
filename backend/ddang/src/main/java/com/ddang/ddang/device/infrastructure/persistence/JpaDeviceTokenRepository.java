package com.ddang.ddang.device.infrastructure.persistence;

import com.ddang.ddang.device.domain.DeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaDeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    Optional<DeviceToken> findByUserId(final Long userId);

    void deleteByUserId(final Long id);
}
