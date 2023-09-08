package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.UserDeviceToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserDeviceTokenRepository extends JpaRepository<UserDeviceToken, Long> {
}
