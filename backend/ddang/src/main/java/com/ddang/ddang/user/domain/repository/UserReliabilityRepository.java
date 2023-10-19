package com.ddang.ddang.user.domain.repository;

import com.ddang.ddang.user.domain.UserReliability;

import java.util.Optional;

public interface UserReliabilityRepository {

    UserReliability save(final UserReliability userReliability);

    Optional<UserReliability> findByUserId(final Long userId);
}
