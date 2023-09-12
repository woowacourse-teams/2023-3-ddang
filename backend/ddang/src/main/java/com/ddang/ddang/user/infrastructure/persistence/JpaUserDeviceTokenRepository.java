package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {

    Optional<UserDeviceToken> findByUserId(final Long userId);
}
