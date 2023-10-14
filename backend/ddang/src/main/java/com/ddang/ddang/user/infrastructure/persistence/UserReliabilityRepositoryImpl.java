package com.ddang.ddang.user.infrastructure.persistence;

import com.ddang.ddang.user.domain.UserReliability;
import com.ddang.ddang.user.domain.repository.UserReliabilityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserReliabilityRepositoryImpl implements UserReliabilityRepository {

    private final JpaUserReliabilityRepository jpaUserReliabilityRepository;

    @Override
    public UserReliability save(final UserReliability userReliability) {
        return jpaUserReliabilityRepository.save(userReliability);
    }

    @Override
    public Optional<UserReliability> findByUserId(final Long userId) {
        return jpaUserReliabilityRepository.findByUserId(userId);
    }
}
