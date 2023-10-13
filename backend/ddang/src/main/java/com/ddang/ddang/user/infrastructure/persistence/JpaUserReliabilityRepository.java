package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.UserReliability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaUserReliabilityRepository extends JpaRepository<UserReliability, Long> {

    Optional<UserReliability> findByUserId(final Long userId);
}
